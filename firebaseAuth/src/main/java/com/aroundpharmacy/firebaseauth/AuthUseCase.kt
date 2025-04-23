package com.aroundpharmacy.firebaseauth

import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun login (email : String, password: String): Result<Boolean>{
        return authRepository.loginWithEmail(email, password)
    }
    suspend fun signUp(name : String?, email:String , password: String): Result<Boolean>{
        return authRepository.signUpWithEmail(name , email , password)
    }
    suspend fun logout(): Result<Boolean>{
        return authRepository.logout()
    }
    suspend fun deleteUser(): Result<Boolean>{
        return authRepository.deleteUser()
    }

}