package com.aroundpharmacy.app.view.home

import android.Manifest
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.adapter.PharmacyAdapter
import com.aroundpharmacy.app.databinding.FragmentAroundPharmacyBinding
import com.aroundpharmacy.app.model.PharmacyDto
import com.aroundpharmacy.app.view.BaseFragment
import com.aroundpharmacy.app.viewModel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LabelTextStyle
import com.kakao.vectormap.shape.Polygon


class AroundPharmacyFragment : BaseFragment() {
    override var isBackAvailable: Boolean = false
    private lateinit var binding: FragmentAroundPharmacyBinding
    private lateinit var mapViewModel: MapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap

    private var animationPolygon: Polygon? = null

    private lateinit var labelLayer: LabelLayer
    private lateinit var centerLabel: Label
    private lateinit var myCurrentLocationBtn: FloatingActionButton

    private var selectedLabel: Label? = null

    private lateinit var pharmacyList: List<PharmacyDto>


    private val defaultBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ic_pharmacy_pin)
    }
    private val selectedBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ic_pharmacy_pin_selected)
    }

    private val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        val grantedList = isGranted.filter { it.value }.map { it.key }
        mapViewModel.onPermissionsResult(grantedList)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAroundPharmacyBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        mapViewModel.onFragmentLoaded()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myCurrentLocationBtn = binding.btnMyLocation
        binding.btnKeyword.setOnClickListener {
            performSearch(binding.editKeyword.text.toString())
        }
        setupObservers()
        setupLocationButton()

    }

    private fun performSearch(keyword: String) {
        if (keyword.length < 2) {
            Toast.makeText(requireContext(), "두 글자 이상 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                val result = mapViewModel.searchPharmacies(
                    keyword,
                    loc.latitude, loc.longitude
                )
                showPharmacyBottomSheet(result)
            } else {
                Toast.makeText(requireContext(), "현재 위치를 가져올 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        mapViewModel.requestLocationPermission.observe(viewLifecycleOwner) { shouldRequest ->
            if (shouldRequest) requestPermissionLauncher.launch(permissionList)
        }

        mapViewModel.isPermissionGranted.observe(viewLifecycleOwner) { granted ->
            if (Manifest.permission.ACCESS_COARSE_LOCATION in granted ||
                Manifest.permission.ACCESS_FINE_LOCATION in granted
            ) {
                mapViewModel.fetchCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 없습니다", Toast.LENGTH_SHORT).show()
            }
        }

        mapViewModel.currentLocation.observe(viewLifecycleOwner) { (lat, lng) ->
            mapStart(lat, lng)
        }
    }

    private fun setupLocationButton() {
        myCurrentLocationBtn.apply {
            isEnabled = false
            alpha = 0.4f
            setOnClickListener {
                isEnabled = false
                alpha = 0.4f
                mapViewModel.fetchCurrentLocation()
            }
        }
    }

    private fun mapStart(lat: Double, lng: Double) {
        mapView = binding.kakaoMap
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception) {}
        }, object : KakaoMapReadyCallback() {

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                labelLayer = kakaoMap.labelManager?.layer!!
                val pos = kakaoMap.cameraPosition?.position
                mapViewModel.fetchNearby(lat, lng)
                setupMapListeners()
                if (pos != null) {
                    observePharmacyData(pos)
                }
            }

            override fun getPosition(): LatLng = LatLng.from(lat, lng)
            override fun getZoomLevel(): Int = 15
        })

    }

    private fun setupMapListeners() {
        kakaoMap.setOnCameraMoveStartListener { _, gestureType ->
            if (gestureType != GestureType.Unknown) {
                myCurrentLocationBtn.isEnabled = true
                myCurrentLocationBtn.alpha = 1f
            }
        }

        kakaoMap.setOnCameraMoveEndListener { _, _, _ ->
            if (binding.btnRefreshLocation.visibility == View.GONE) {
                binding.btnRefreshLocation.visibility = View.VISIBLE
            }
        }

        binding.btnRefreshLocation.setOnClickListener {
            val center = kakaoMap.cameraPosition!!.position
            mapViewModel.fetchNearby(center.latitude, center.longitude)
            binding.btnRefreshLocation.visibility = View.GONE
        }


        /** 라벨을 누르면 약국의 이름이 나오고 그 이름을 클릭하면 약국 정보가 나오도록 했습니다.*/
        kakaoMap.setOnLabelClickListener { _, labelLayer, label ->
            val pharmacy = label.tag as? PharmacyDto ?: return@setOnLabelClickListener true
            labelLayer.remove(label)
            if (selectedLabel == label) {
                showPharmacyBottomSheet(listOf(pharmacy))
                selectedLabel = null
            }
            createSelectedLabel(pharmacy)
            true
        }
    }


    private fun observePharmacyData(pos: LatLng) {
        mapViewModel.pharmacies.observe(viewLifecycleOwner) { list ->
            labelLayer.removeAll()
            pharmacyList = list

            createLabels(pos)
            createPharmacyLabels(pharmacyList)
        }
    }

    /**현재위치를 나타냅니다 */
    private fun createLabels(pos: LatLng) {
        centerLabel = labelLayer.addLabel(
            LabelOptions.from("dotLabel", pos)
                .setStyles(LabelStyle.from(R.drawable.marker).setAnchorPoint(0.5f, 0.5f))
                .setRank(1)
        )!!
        centerLabel.addShareTransform(animationPolygon)
    }
    /**약국 위치를 나타냅니다. */
    private fun createPharmacyLabels(list: List<PharmacyDto>) {
        list.forEach { pharmacy ->
            val position = LatLng.from(pharmacy.lat, pharmacy.lon)

            val style = LabelStyle.from(defaultBitmap)

            val options = LabelOptions.from(position)
                .setStyles(style)
                .setClickable(true)       // 클릭 허용
                .setTag(pharmacy)
            labelLayer.addLabel(options)

        }
    }

    /** 선택한 곳에 새로운 라벨을 만들어 약국 이름과 함께 표시하는 메서드*/
    private fun createSelectedLabel(pharmacy : PharmacyDto){
        selectedLabel?.let { prev ->
            labelLayer.remove(prev)
            val normalOpt = LabelOptions.from(prev.position)
                .setStyles(LabelStyle.from(defaultBitmap))
                .setTag(prev.tag)
                .setClickable(true)
            labelLayer.addLabel(normalOpt)
        }
        val textStyle = LabelTextStyle.from(
            22, Color.BLACK, 4,
            Color.WHITE
        )
        val textBuilder = LabelTextBuilder().addTextLine(pharmacy.name, 0)
        val selectedStyle = LabelStyle.from(textStyle)
            .setTextGravity(Gravity.TOP)
            .setAnchorPoint(0.5f, 1.0f)
        selectedStyle.iconStyle.bitmap = selectedBitmap

        val newLabel = labelLayer.addLabel(
            LabelOptions.from(LatLng.from(pharmacy.lat, pharmacy.lon))
                .setStyles(selectedStyle)
                .setTexts(textBuilder)
                .setTag(pharmacy)
                .setClickable(true)
        )

        selectedLabel = newLabel
        val position = LatLng.from(pharmacy.lat, pharmacy.lon)
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position, 17)
        kakaoMap.moveCamera(cameraUpdate)
    }


    /** 바텀 다이얼로그입니다. */
    private fun showPharmacyBottomSheet(pharmacies: List<PharmacyDto>) {
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_pharmacy, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.pharmacyRecyclerView)
        val txtEmpty = dialogView.findViewById<TextView>(R.id.txt_empty)

        val dialog = BottomSheetDialog(requireContext())
        val adapter = PharmacyAdapter(pharmacies) { pharmacy ->
            createSelectedLabel(pharmacy)
            dialog.dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        fun toggleEmptyState() {
            if (adapter.itemCount == 0) {
                txtEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                txtEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }
        toggleEmptyState()
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() = toggleEmptyState()
        })

        dialog.setContentView(dialogView)
        dialog.show()
    }



}