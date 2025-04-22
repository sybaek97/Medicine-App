package com.aroundpharmacy.app.model

import com.google.gson.annotations.SerializedName

data class DrugPrdtPrmsnInfoResponse(
    @SerializedName("header") val header: DrugPrdtPrmsnInfoResponseHeader,
    @SerializedName("body")   val body: DrugPrdtPrmsnInfoResponseBody
)
data class DrugPrdtPrmsnInfoResponseHeader(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg")  val resultMsg: String
)

data class DrugPrdtPrmsnInfoResponseBody(
    @SerializedName("items")      val items: List<DrugPrdtPrmsnInfoResponseDto>,
    @SerializedName("numOfRows")  val numOfRows: Int,
    @SerializedName("pageNo")     val pageNo: Int,
    @SerializedName("totalCount") val totalCount: Int
)


data class DrugPrdtPrmsnInfoResponseDto(
    //업일련번호
    @SerializedName("ITEM_SEQ")
    val itemSeq: String,

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
    @SerializedName("BIG_PRDT_IMG_URL")
    val itemIngrName: String,

    )