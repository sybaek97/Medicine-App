package com.aroundpharmacy.app.viewModel


import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aroundpharmacy.app.api.KakaoRetrofitClient
import com.aroundpharmacy.app.model.PharmacyDto
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch


class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _requestLocationPermission = MutableLiveData<Boolean>()
    val requestLocationPermission : LiveData<Boolean> = _requestLocationPermission
    private val _isPermissionGranted = MutableLiveData<List<String>>()
    val isPermissionGranted: LiveData<List<String>> = _isPermissionGranted
    private val _currentLocation = MutableLiveData<Pair<Double, Double>>()
    val currentLocation : LiveData<Pair<Double,Double>> = _currentLocation
    private val _pharmacies = MutableLiveData<List<PharmacyDto>>()
    val pharmacies: LiveData<List<PharmacyDto>> = _pharmacies
    private val _searchPharmacies = MutableLiveData<List<PharmacyDto>>()
    val searchPharmacies: LiveData<List<PharmacyDto>> = _searchPharmacies

    fun onFragmentLoaded() {
        _requestLocationPermission.value = true
    }

    fun onPermissionsResult(granted: List<String>) {
        _isPermissionGranted.value = granted
    }

    fun fetchCurrentLocation(){
        val context = getApplication<Application>()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location->
            if(location != null){
                _currentLocation.value = location.latitude to location.longitude
            }
        }
    }
    fun fetchNearby(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val resp = KakaoRetrofitClient.api
                    .searchPlacesByKeyword("약국", lat, lon, 20000)
                _pharmacies.value = resp.documents
            } catch (e: Exception) {
                Log.d("Pharmacy", "에러? $e")
                e.printStackTrace()
            }
        }
    }

    fun searchPharmacies(
        keyword: String,
        centerLat: Double,
        centerLon: Double,
        radiusKm: Double = 20.0
    ): List<PharmacyDto> {

        if (keyword.length < 2) return emptyList()

        return _pharmacies.value.orEmpty().filter { p ->
            p.name.contains(keyword, ignoreCase = true) &&
                    distanceKm(centerLat, centerLon, p.lat, p.lon) <= radiusKm
        }
    }

    private fun distanceKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000.0     // m → km
    }
}