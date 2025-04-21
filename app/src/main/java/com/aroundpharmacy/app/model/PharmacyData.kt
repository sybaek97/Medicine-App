package com.aroundpharmacy.app.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val documents: List<PharmacyDto>
)

data class PharmacyDto(
    @SerializedName("place_name")
    val name: String,
    @SerializedName("road_address_name")
    val address: String,
    @SerializedName("y")
    val lat: Double,
    @SerializedName("x")
    val lon: Double
)