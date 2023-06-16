package com.rayhan.pasarkitarevision.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.rayhan.pasarkitarevision.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
            .readTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    @Named("ApiService")
    fun provideApiService(builder: Retrofit.Builder): ApiService {
        val retrofit = builder.baseUrl("http://34.136.73.74:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    @Named("ApiServiceRecommendation")
    fun provideApiServiceRecommendation(builder: Retrofit.Builder): ApiService {
        val retrofit = builder.baseUrl("https://recomendation-et6b6qs4tq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }
}





