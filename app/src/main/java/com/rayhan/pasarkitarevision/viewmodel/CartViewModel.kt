package com.rayhan.pasarkitarevision.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rayhan.pasarkitarevision.data.repository.CartRepository
import com.rayhan.pasarkitarevision.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    fun addToCart(cartItem : CartItem) {
        val item = CartItem(
            image = cartItem.image,
            name = cartItem.name,
            satuan = cartItem.satuan,
            price = cartItem.price
        )
        cartRepository.addToCart(item)

    }

    fun removeFromCart(item: CartItem) {
        cartRepository.removeFromCart(item)
    }

    fun getAllCartItems(): LiveData<List<CartItem>?> {
        return cartRepository.getAllCartItems()
    }
}
