package com.rayhan.pasarkitarevision.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rayhan.pasarkitarevision.databinding.ItemProdukBinding
import com.rayhan.pasarkitarevision.model.ProductItem
import java.text.NumberFormat
import java.util.*

class ProductSearchAdapter(private val itemClickListener: (Int) -> Unit) :
    ListAdapter<ProductItem, ProductSearchAdapter.RecommendationViewHolder>(diffCallback) {

    inner class RecommendationViewHolder(val binding: ItemProdukBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ProductItem>() {
            override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        return RecommendationViewHolder(
            ItemProdukBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.binding.apply {
            tvNamaProduk.text = currentProduct.name
            tvSatuanProduk.text = "/ ${currentProduct.satuan} "
            tvHargaProduk.text = formatPrice(currentProduct.price)

            Glide.with(imgProduk)
                .load(currentProduct.image)
                .into(imgProduk)

            holder.itemView.setOnClickListener {
                val productId = currentProduct.id
                itemClickListener(productId)
            }
        }
    }
    fun formatPrice(price: Int): String {
        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(price.toLong()).replace(",00", "")
    }

}