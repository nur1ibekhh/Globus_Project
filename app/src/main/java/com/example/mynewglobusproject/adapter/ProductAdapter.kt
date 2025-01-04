package com.example.mynewglobusproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.model.Product

class ProductAdapter(private var productList: MutableList<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "${product.price} сум"

        // Rasmlarni yuklash
        if (product.images.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(product.images[0].image)
                .into(holder.imageView)
        }
    }

    // Yangi mahsulotlarni qo'shish uchun metod
    fun addProducts(newProducts: List<Product>) {
        productList.addAll(newProducts) // Mavjud ro'yxatga yangi mahsulotlarni qo'shish
        notifyDataSetChanged() // Adapterni yangilash
    }


    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}