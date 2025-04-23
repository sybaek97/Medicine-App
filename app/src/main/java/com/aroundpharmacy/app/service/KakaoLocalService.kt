package com.aroundpharmacy.app.service

import com.aroundpharmacy.app.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlacesByKeyword(
        @Query("query") query: String,
        @Query("y") latitude: Double? = null,
        @Query("x") longitude: Double? = null,
        @Query("radius") radius: Int? = null,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
        @Query("sort") sort: String = "accuracy"
    ): SearchResponse

}