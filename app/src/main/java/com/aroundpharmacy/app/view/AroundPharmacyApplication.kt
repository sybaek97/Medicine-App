package com.aroundpharmacy.app.view

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import java.security.MessageDigest

@HiltAndroidApp
class AroundPharmacyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }


}
