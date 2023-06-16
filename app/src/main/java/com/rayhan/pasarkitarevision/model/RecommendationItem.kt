package com.rayhan.pasarkitarevision.model

import com.google.gson.annotations.SerializedName

data class RecommendationItem(
    @SerializedName("ID") val id: Int,
    @SerializedName("Img") val img: String,
    @SerializedName("Nama") val name: String,
    @SerializedName("Kategori") val category: String,
    @SerializedName("Lokasi") val location: String,
    @SerializedName("Harga") val price: String
)
