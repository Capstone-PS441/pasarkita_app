package com.rayhan.pasarkitarevision.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rayhan.pasarkitarevision.data.repository.ProductRepository
import com.rayhan.pasarkitarevision.model.ProductItem
import com.rayhan.pasarkitarevision.model.RecommendationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {
    private val _recommendation = MutableLiveData<List<RecommendationItem>>()
    val recommendation: LiveData<List<RecommendationItem>>
        get() = _recommendation

    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>>
        get() = _products

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem>
        get() = _product

    fun fetchRecommendations(productId: Int? = null) {
        viewModelScope.launch {
            try {
                val response = repository.getRecommendation()
                if (response.isSuccessful) {
                    val recommendations = response.body()
                    recommendations?.let {
                        val filteredProducts = productId?.let { id ->
                            it.filter { product -> product.id != id }
                        } ?: it
                        _recommendation.value = filteredProducts
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

    fun fetchProductById(productId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductById(productId)
                if (response.isSuccessful) {
                    val product = response.body()
                    product?.let {
                        _product.value = it
                        Log.d("ProductViewModel", "Product: $it")
                    }
                } else {
                    Log.e("ProductViewModel", "Failed to fetch product: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun fetchProductsBySellerId(sellerId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful) {
                    val products = response.body()
                    products?.let {
                        val filteredProducts = it.filter { product ->
                            product.seller?.id == sellerId
                        }
                        _products.value = filteredProducts
                        Log.d("ProductViewModel", "Products: $filteredProducts")
                    }
                } else {
                    Log.e("ProductViewModel", "Failed to fetch products: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}", e)
            }
        }
    }


    fun fetchSearchResults(searchQuery: String): LiveData<List<ProductItem>> {
        val searchResults = MutableLiveData<List<ProductItem>>()
        viewModelScope.launch {
            try {
                val response = repository.getAllProducts()
                if (response.isSuccessful) {
                    val recommendations = response.body()
                    recommendations?.let {
                        val filteredResults = recommendations.filter { recommendation ->
                            recommendation.name.contains(searchQuery, ignoreCase = true)
                        }
                        searchResults.value = filteredResults
                    }
                } else {
                    Log.e("ProductViewModel", "Failed to fetch search results: ${response.message()}")
                    searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}", e)
                searchResults.value = emptyList()
            }
        }
        return searchResults
    }
}
