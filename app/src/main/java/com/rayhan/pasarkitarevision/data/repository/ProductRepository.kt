package com.rayhan.pasarkitarevision.data.repository

import com.rayhan.pasarkitarevision.data.remote.ApiService
import com.rayhan.pasarkitarevision.model.ProductItem
import com.rayhan.pasarkitarevision.model.RecommendationItem
import com.rayhan.pasarkitarevision.model.SellerItem
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class ProductRepository @Inject constructor(
    @Named("ApiService") private val apiService: ApiService,
    @Named("ApiServiceRecommendation") private val apiServiceRecommendation: ApiService
) {
    suspend fun getRecommendation(): Response<ArrayList<RecommendationItem>> {
        return apiServiceRecommendation.getRecommendation()
    }

    suspend fun getAllProducts(): Response<ArrayList<ProductItem>> {
        return apiService.getAllProducts()
    }

    suspend fun getProductById(id: Int): Response<ProductItem> {
        return apiService.getProductById(id)
    }

    suspend fun getSellerById(id: Int): Response<SellerItem> {
        return apiService.getSellerById(id)
    }
}