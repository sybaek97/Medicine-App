package com.aroundpharmacy.firebaseauth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : AuthRepository{

    /** 로그인 */
    override suspend fun loginWithEmail(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email,password).await()
            Result.success(true)
        }catch ( e: Exception){
            Result.failure(e)
        }
    }

    /** 회원가입 */
    override suspend fun signUpWithEmail(
        name: String?,
        email: String,
        password: String
    ): Result<Boolean> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()

            if(!name.isNullOrEmpty()) {
                val user = result.user
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user?.updateProfile(profileUpdates)?.await()
            }
            Result.success(true)

        }catch (e:Exception ){
            Result.failure(e)
        }
    }

    /** 로그아웃 */
    override suspend fun logout(): Result<Boolean> {
        return try{
            FirebaseAuth.getInstance().signOut()
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
    /** 회원탈퇴 */
    override suspend fun deleteUser(): Result<Boolean> {

        return try {
            val user = firebaseAuth.currentUser
                ?: return Result.failure(IllegalStateException("로그인된 사용자가 없습니다."))
            user.delete().await()

            Result.success(true)

        }catch(e: Exception){
            Result.failure(e)
        }
    }
}