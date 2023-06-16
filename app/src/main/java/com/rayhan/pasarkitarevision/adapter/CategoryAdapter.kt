package com.rayhan.pasarkitarevision.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rayhan.pasarkitarevision.R
import com.rayhan.pasarkitarevision.model.CategoryItem

class CategoryAdapter(private val categories: List<CategoryItem>, private val onItemClick: (CategoryItem) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categories[position]
        holder.bind(categoryItem)
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryImage: ImageView = itemView.findViewById(R.id.ic_category)
        private val categoryText: TextView = itemView.findViewById(R.id.tv_category)

        fun bind(categoryItem: CategoryItem) {
            categoryImage.setImageResource(categoryItem.imageResId)
            categoryText.text = categoryItem.name

            itemView.setOnClickListener {
                onItemClick(categoryItem)
            }
        }
    }
}
