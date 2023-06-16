package com.rayhan.pasarkitarevision.ui.produk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProdukViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is produk Fragment"
    }
    val text: LiveData<String> = _text
}