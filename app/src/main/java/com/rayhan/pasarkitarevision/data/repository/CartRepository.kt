package com.rayhan.pasarkitarevision.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rayhan.pasarkitarevision.model.CartItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor() {
    private val cartItems: MutableLiveData<List<CartItem>?> = MutableLiveData()

    init {
        cartItems.value = mutableListOf()
    }

    fun addToCart(cartItem: CartItem) {
        val currentItems = cartItems.value?.toMutableList() ?: mutableListOf()
        currentItems.add(cartItem)
        cartItems.value = currentItems
    }

    fun removeFromCart(cartItem: CartItem) {
        val currentItems = cartItems.value?.toMutableList()
        currentItems?.remove(cartItem)
        cartItems.value = currentItems
    }

    fun getAllCartItems(): LiveData<List<CartItem>?> {
        return cartItems
    }
}
