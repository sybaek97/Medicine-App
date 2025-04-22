package com.aroundpharmacy.app.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header

data class DrugPrdtPrmsnInfoResponse(
    @SerializedName("header") val header: Header,
    @SerializedName("body")   val body: Body
)
data class Body(
    @SerializedName("items")      val items: Items,
    @SerializedName("numOfRows")  val numOfRows: Int,
    @SerializedName("pageNo")     val pageNo: Int,
    @SerializedName("totalCount") val totalCount: Int
)
data class Items(
    @SerializedName("item") val itemList: List<DrugPrdtPrmsnInfoResponseDto>
)


data class DrugPrdtPrmsnInfoResponseDto(
    //업일련번호
    @SerializedName("ENTP_SEQ")
    val entpSeq: String,

    //업체명
    @SerializedName("ENTP_NAME")
    val entpName: String,

    //약 이름
    @SerializedName("ITEM_NAME")
    val itemName: String,

    //약 이름(영문)
    @SerializedName("ITEM_ENG_NAME")
    val itemEngName: String,

    //전문/일반의약품 구분
    @SerializedName("SPCLTY_PBLC")
    val spcltyPblc: String,

    //상품 분류
    @SerializedName("PRDUCT_TYPE")
    val productType: String,

    //이미지 url
    @SerializedName("ITEM_INGR_NAME")
    val itemIngrName: String,

)