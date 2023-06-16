package com.rayhan.pasarkitarevision.model

data class SellerItem(
    val id: Int,
    val name: String,
    val location: String,
    val image: String,
    val category: String?,
    val items: List<ProductItem>?,
    val createdAt: String,
    val updatedAt: String
)
