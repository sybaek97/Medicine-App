package com.aroundpharmacy.app.service

import com.aroundpharmacy.app.model.DrugPrdtPrmsnInfoResponse
import com.aroundpharmacy.app.model.MdcinGrnIdntfcInfoResponse
import com.aroundpharmacy.app.model.SearchResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DrugPrdtPrmsnInfoService {


    @GET("1471000/DrugPrdtPrmsnInfoService06/getDrugPrdtPrmsnInq06")
    suspend fun getDrugList(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo")     pageNo: Int,
        @Query("numOfRows")  numOfRows: Int,
        @Query("item_name")  itemName: String? = null,
        @Query("type")       type: String = "json"
    ): DrugPrdtPrmsnInfoResponse

    @GET("1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01")
    suspend fun getMedicineList(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo")     pageNo: Int = 1,
        @Query("numOfRows")  numOfRows: Int = 1,
        @Query("item_seq")  itemSeq: String,
        @Query("type")       type: String = "json"
    ): MdcinGrnIdntfcInfoResponse

}