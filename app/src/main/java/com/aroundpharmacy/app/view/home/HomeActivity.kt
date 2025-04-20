package com.aroundpharmacy.app.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.ActivityHomeBinding
import com.aroundpharmacy.app.BuildConfig
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val KAKAO_MAP_KEY = BuildConfig.KAKAO_API_KEY

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_home) as NavHostFragment
        val navController = navHostFragment.navController
        KakaoMapSdk.init(this, KAKAO_MAP_KEY)

        binding.bottomNav.setupWithNavController(navController)

    }
}