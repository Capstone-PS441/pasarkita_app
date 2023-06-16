package com.rayhan.pasarkitarevision.data.remote

import com.rayhan.pasarkitarevision.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/recommend/1111")
    suspend fun getRecommendation(): Response<ArrayList<RecommendationItem>>

    @GET("api/products")
    suspend fun getAllProducts(): Response<ArrayList<ProductItem>>

    @GET("/api/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductItem>

    @GET("api/sellers/{id}")
    suspend fun getSellerById(@Path("id") id: Int): Response<SellerItem>

    @POST("auth/signup")
    suspend fun registerUser(@Body request: UserRegistrationRequest): Response<RegisterResponse>

    @POST("auth/signin")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<SignInResponse>
}
