package com.rayhan.pasarkitarevision.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ProductItem(
    val image: String,
    val createdAt: String,
    val price: Int,
    val satuan: String,
    val name: String,
    val toko: String,
    val location: String,
    val id: Int,
    val category: String,
    val updatedAt: String,
    val sellerId: Int,
    val seller: SellerItem
)
