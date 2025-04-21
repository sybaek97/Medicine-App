package com.aroundpharmacy.app.view.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.ActivityHomeBinding
import com.aroundpharmacy.app.BuildConfig
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val KAKAO_MAP_KEY = BuildConfig.KAKAO_API_KEY
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        printKeyHash()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_home) as NavHostFragment
        val navController = navHostFragment.navController
        KakaoMapSdk.init(this, KAKAO_MAP_KEY)

        binding.bottomNav.setupWithNavController(navController)

    }


    /** kakao 해시 키 얻는 함수입니다.*/
    fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (sig in info.signatures!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(sig.toByteArray())
                val hash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KeyHash", hash)  // 로그캣에 찍힌 값을 복사해서 카카오 디벨로퍼스에 등록
            }
        } catch (e: Exception) {
            Log.e("KeyHash", "해시키 생성 실패", e)
        }
    }
}