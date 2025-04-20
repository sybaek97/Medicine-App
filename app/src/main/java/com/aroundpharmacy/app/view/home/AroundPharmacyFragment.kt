package com.aroundpharmacy.app.view.home

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.FragmentAroundPharmacyBinding
import com.aroundpharmacy.app.view.BaseFragment
import com.aroundpharmacy.app.viewModel.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.animation.Interpolation
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.shape.DotPoints
import com.kakao.vectormap.shape.Polygon
import com.kakao.vectormap.shape.PolygonOptions
import com.kakao.vectormap.shape.PolygonStyles
import com.kakao.vectormap.shape.PolygonStylesSet
import com.kakao.vectormap.shape.ShapeAnimator
import com.kakao.vectormap.shape.animation.CircleWave
import com.kakao.vectormap.shape.animation.CircleWaves


class AroundPharmacyFragment : BaseFragment() {
    override var isBackAvailable: Boolean = false
    private lateinit var binding: FragmentAroundPharmacyBinding
    private lateinit var mapViewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var kakaoMap: KakaoMap
    private var animationPolygon: Polygon? = null
    private var labelLayer: LabelLayer? = null
    private var centerLabel: Label? =  null
    private lateinit var myCurrentLocationBtn : FloatingActionButton

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
        myCurrentLocationBtn.isEnabled = false         // 초기 비활성
        myCurrentLocationBtn.alpha    = 0.4f
        myCurrentLocationBtn.setOnClickListener {
            myCurrentLocationBtn.isEnabled = false         // 초기 비활성
            myCurrentLocationBtn.alpha    = 0.4f
            mapViewModel.fetchCurrentLocation()

        }



        mapViewModel.requestLocationPermission.observe(viewLifecycleOwner) { shouldRequest ->
            if (shouldRequest) {
                requestPermissionLauncher.launch(permissionList)
            }
        }

        // 결과 처리
        mapViewModel.isPermissionGranted.observe(viewLifecycleOwner) { granted ->
            when {
                Manifest.permission.ACCESS_COARSE_LOCATION in granted ||Manifest.permission.ACCESS_FINE_LOCATION in granted -> {
                    mapViewModel.fetchCurrentLocation()


                }

                else -> {
                    Toast.makeText(requireContext(), "위치 권한이 없습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mapViewModel.currentLocation.observe(viewLifecycleOwner) { (lat, lng) ->
            mapStart(lat, lng)
        }

    }

    private fun mapStart(lat : Double, lng : Double) {
        mapView = binding.kakaoMap
        mapView.start(object : MapLifeCycleCallback() {override fun onMapDestroy() {}override fun onMapError(error: Exception) {} }, object : KakaoMapReadyCallback() {

            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                labelLayer = kakaoMap.labelManager!!.layer
                val pos = kakaoMap.cameraPosition!!.position

                createLabels(pos)
                kakaoMap.setOnCameraMoveStartListener { _, gestureType ->
                    if (gestureType != GestureType.Unknown) {
                        myCurrentLocationBtn.isEnabled = true
                        myCurrentLocationBtn.alpha = 1f
                    }
                }
            }

            override fun getPosition(): LatLng {
                return LatLng.from(lat, lng);
            }

            override fun getZoomLevel(): Int {
                return 15
            }

        })

    }
    private fun createLabels(pos: LatLng) {
        centerLabel = labelLayer?.addLabel(
            LabelOptions.from("dotLabel", pos)
                .setStyles(LabelStyle.from(R.drawable.marker).setAnchorPoint(0.5f, 0.5f))
                .setRank(1)
        )
        centerLabel?.addShareTransform(animationPolygon)
    }


}