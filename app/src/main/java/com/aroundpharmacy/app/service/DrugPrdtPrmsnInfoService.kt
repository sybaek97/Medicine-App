package com.aroundpharmacy.app.service

import com.aroundpharmacy.app.model.DrugPrdtPrmsnInfoResponse
import com.aroundpharmacy.app.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DrugPrdtPrmsnInfoService {


    @GET("1471000/DrugPrdtPrmsnInfoService06/getDrugPrdtPrmsnInq06")
    suspend fun getMedicineList(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo")     pageNo: Int,
        @Query("numOfRows")  numOfRows: Int,
        @Query("item_name")  itemName: String? = null,
        @Query("type")       type: String = "json"
    ): DrugPrdtPrmsnInfoResponse

}