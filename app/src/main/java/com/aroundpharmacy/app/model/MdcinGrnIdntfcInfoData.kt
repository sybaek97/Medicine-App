package com.aroundpharmacy.app.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header

data class MdcinGrnIdntfcInfoResponse(
    @SerializedName("header") val header: MdcinGrnIdntfcInfoResponseHeader,
    @SerializedName("body")   val body: MdcinGrnIdntfcInfoResponseBody
)
data class MdcinGrnIdntfcInfoResponseHeader(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg")  val resultMsg: String
)

data class MdcinGrnIdntfcInfoResponseBody(
    @SerializedName("items")      val items: List<MdcinGrnIdntfcInfoResponseDto>?,
    @SerializedName("numOfRows")  val numOfRows: Int,
    @SerializedName("pageNo")     val pageNo: Int,
    @SerializedName("totalCount") val totalCount: Int
)


data class MdcinGrnIdntfcInfoResponseDto(
    @SerializedName("ITEM_SEQ")            val itemSeq: String,
    @SerializedName("ITEM_NAME")           val itemName: String,
    @SerializedName("ENTP_SEQ")            val entpSeq: String,
    @SerializedName("ENTP_NAME")           val entpName: String,
    @SerializedName("CHART")               val chart: String,
    @SerializedName("ITEM_IMAGE")          val itemImage: String,
    @SerializedName("PRINT_FRONT")         val printFront: String?,
    @SerializedName("PRINT_BACK")          val printBack: String?,
    @SerializedName("DRUG_SHAPE")          val drugShape: String,
    @SerializedName("COLOR_CLASS1")        val colorClass1: String,
    @SerializedName("COLOR_CLASS2")        val colorClass2: String?,
    @SerializedName("LINE_FRONT")          val lineFront: String?,
    @SerializedName("LINE_BACK")           val lineBack: String?,
    @SerializedName("LENG_LONG")           val lengthLong: String,
    @SerializedName("LENG_SHORT")          val lengthShort: String,
    @SerializedName("THICK")               val thickness: String,
    @SerializedName("IMG_REGIST_TS")       val imgRegistTs: String,
    @SerializedName("CLASS_NO")            val classNo: String,
    @SerializedName("CLASS_NAME")          val className: String,
    @SerializedName("ETC_OTC_NAME")        val etcOtcName: String,
    @SerializedName("ITEM_PERMIT_DATE")    val itemPermitDate: String,
    @SerializedName("FORM_CODE_NAME")      val formCodeName: String,
    @SerializedName("MARK_CODE_FRONT_ANAL")val markCodeFrontAnal: String?,
    @SerializedName("MARK_CODE_BACK_ANAL") val markCodeBackAnal: String?,
    @SerializedName("MARK_CODE_FRONT_IMG") val markCodeFrontImg: String?,
    @SerializedName("MARK_CODE_BACK_IMG")  val markCodeBackImg: String?,
    @SerializedName("ITEM_ENG_NAME")       val itemEngName: String?,
    @SerializedName("CHANGE_DATE")         val changeDate: String,
    @SerializedName("MARK_CODE_FRONT")     val markCodeFront: String?,
    @SerializedName("MARK_CODE_BACK")      val markCodeBack: String?,
    @SerializedName("EDI_CODE")            val ediCode: String?,
    @SerializedName("BIZRNO")              val bizrno: String

)