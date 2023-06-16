package com.rayhan.pasarkitarevision.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rayhan.pasarkitarevision.data.repository.ProductRepository
import com.rayhan.pasarkitarevision.model.ProductItem
import com.rayhan.pasarkitarevision.model.SellerItem
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _seller = MutableLiveData<SellerItem>()
    val seller: LiveData<SellerItem> get() = _seller

    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>>
        get() = _products

    fun fetchSellerById(sellerId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getSellerById(sellerId)
                if (response.isSuccessful) {
                    val seller = response.body()
                    seller?.let {
                        _seller.value = it
                        Log.d("SellerViewModel", "Seller: $seller")
                    }
                } else {
                    Log.e("SellerViewModel", "Failed to fetch seller: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("SellerViewModel", "Exception: ${e.message}", e)
            }
        }
    }
    fun fetchProducts(productId: Int? = null) {
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful) {
                    val recommendations = response.body()
                    recommendations?.let {
                        val filteredProducts = productId?.let { id ->
                            it.filter { product -> product.id != id }
                        } ?: it
                        _products.value = filteredProducts
                        Log.d("ProductViewModel", "Recommendations: $filteredProducts")
                    }
                } else {
                    Log.e("ProductViewModel", "Failed to fetch recommendations: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}


