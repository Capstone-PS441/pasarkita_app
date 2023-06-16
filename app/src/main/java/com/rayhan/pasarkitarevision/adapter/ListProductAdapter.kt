package com.rayhan.pasarkitarevision.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rayhan.pasarkitarevision.databinding.ItemProdukGridBinding
import com.rayhan.pasarkitarevision.model.ProductItem
import com.rayhan.pasarkitarevision.util.Constant
import java.text.NumberFormat
import java.util.*

class ListProductAdapter (private val itemClickListener: Constant.OnItemClickListener) : RecyclerView.Adapter<ListProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(val binding: ItemProdukGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ProductItem>() {
        override fun areItemsTheSame(
            oldItem: ProductItem,
            newItem: ProductItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductItem,
            newItem: ProductItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var productList: List<ProductItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProdukGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.binding.apply {
            tvNamaProduk.text = currentProduct.name
            tvSatuanProduk.text = "/ ${currentProduct.satuan} "
            tvHargaProduk.text = formatPrice(currentProduct.price)

            Glide.with(imgProduk)
                .load(currentProduct.image)
                .into(imgProduk)

            holder.itemView.setOnClickListener {
                val productId = currentProduct.id
                itemClickListener.onItemClick(productId)
            }
        }
    }

    fun formatPrice(price: Int): String {
        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(price.toLong()).replace(",00", "")
    }

    override fun getItemCount() = productList.size
}