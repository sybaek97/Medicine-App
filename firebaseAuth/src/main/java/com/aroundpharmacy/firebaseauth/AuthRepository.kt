package com.aroundpharmacy.firebaseauth

interface AuthRepository {
    suspend fun loginWithEmail(email:String, password:String): Result<Boolean>
    suspend fun signUpWithEmail(name: String?, email:String, password:String): Result<Boolean>
    suspend fun logout(): Result<Boolean>
    suspend fun deleteUser(): Result<Boolean>

}