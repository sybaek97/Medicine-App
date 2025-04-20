package com.aroundpharmacy.app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroundpharmacy.firebaseauth.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase : AuthUseCase
) : ViewModel() {
    private val _loginResult = MutableLiveData <Result<Boolean>>()
    val loginResult :  LiveData<Result<Boolean>> = _loginResult
    private val _sigUpResult = MutableLiveData <Result<Boolean>>()
    val sigUpResult :  LiveData<Result<Boolean>> = _sigUpResult
    private val _logoutResult = MutableLiveData <Result<Boolean>>()
    val logoutResult :  LiveData<Result<Boolean>> = _logoutResult
    private val _deleteResult = MutableLiveData <Result<Boolean>>()
    val deleteResult :  LiveData<Result<Boolean>> = _deleteResult


    fun login(email : String, password:String){
       viewModelScope.launch {
           val result = authUseCase.login(email, password)
           _loginResult.value = result
       }
    }
    fun signUp(name:String, email:String, password: String){
        viewModelScope.launch {
            val result = authUseCase.signUp(name,email, password)
            _sigUpResult.value = result
        }
    }
    fun logout(){
        viewModelScope.launch {
            val result = authUseCase.logout()
            _logoutResult.value = result
        }
    }
    fun deleteUser(){
        viewModelScope.launch {
            val result = authUseCase.deleteUser()
            _deleteResult.value = result
        }
    }
}