package com.rayhan.pasarkitarevision.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rayhan.pasarkitarevision.databinding.ItemKeranjangBinding
import com.rayhan.pasarkitarevision.model.CartItem
import com.rayhan.pasarkitarevision.model.ProductItem
import java.text.NumberFormat
import java.util.Locale

class CartItemAdapter(
    private val onItemClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartItemAdapter.CartItemViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemKeranjangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartItemViewHolder(private val binding: ItemKeranjangBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.apply {
                tvProductName.text = cartItem.name
                tvQuantity.text = cartItem.satuan
                tvPrice.text = formatPrice(cartItem.price)

                Glide.with(ivProduct)
                    .load(cartItem.image)
                    .into(ivProduct)

                itemView.setOnClickListener {
                    onItemClick(cartItem)
                }
            }
        }
    }

    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(
            oldItem: CartItem,
            newItem: CartItem
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CartItem, newItem: CartItem)
        : Boolean {
            return oldItem == newItem
        }
    }
    private fun formatPrice(price: Int): String {
        val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0
        return formatter.format(price.toLong()).replace(",00", "")
    }
}
