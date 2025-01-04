package com.example.mynewglobusproject.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.model.Product

class ProductForCategoriesAdapter(private var products: MutableList<Product>) : RecyclerView.Adapter<ProductForCategoriesAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_product_for_categories_adapter, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    fun setProducts(newProducts: List<Product>) {
        products.clear() // Eski ro'yxatni tozalash
        products.addAll(newProducts) // Yangi ro'yxatni qo'shish
        notifyDataSetChanged() // Adapterni yangilash
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        private val productDescription: TextView = itemView.findViewById(R.id.productDescription)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = product.price.toString()
            productDescription.text = product.description

            if (product.images.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(product.images[0].image)
                    .into(productImage)
            } else {
                productImage.setImageResource(R.drawable.red_circle)
            }
        }
    }
}


