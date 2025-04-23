package com.aroundpharmacy.app.api

import com.aroundpharmacy.app.BuildConfig
import com.aroundpharmacy.app.service.KakaoLocalService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoRetrofitClient {
    private const val BASE_URL = "https://dapi.kakao.com/"
    private const val API_KEY  = BuildConfig.KAKAO_REST_API_KEY

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val req = chain.request().newBuilder()
                .addHeader("Authorization", "KakaoAK $API_KEY")
                .build()
            chain.proceed(req)
        }
        .build()

    val api: KakaoLocalService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(KakaoLocalService::class.java)
}