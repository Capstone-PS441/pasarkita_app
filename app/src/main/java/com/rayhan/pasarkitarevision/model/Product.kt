package com.rayhan.pasarkitarevision.model

import com.google.gson.annotations.SerializedName

data class Product(
    @field:SerializedName("Product")
    val recommendation: ProductItem
)
