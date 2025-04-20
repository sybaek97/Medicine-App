package com.aroundpharmacy.app.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices


class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val _requestLocationPermission = MutableLiveData<Boolean>()
    val requestLocationPermission : LiveData<Boolean> = _requestLocationPermission
    private val _isPermissionGranted = MutableLiveData<List<String>>()
    val isPermissionGranted: LiveData<List<String>> = _isPermissionGranted

    private val _currentLocation = MutableLiveData<Pair<Double, Double>>()
    val currentLocation : LiveData<Pair<Double,Double>> = _currentLocation



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
}