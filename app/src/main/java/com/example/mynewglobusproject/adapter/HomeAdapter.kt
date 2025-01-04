package com.example.mynewglobusproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewglobusproject.model.Product // Ensure you have the correct import for the Product class
import com.example.mynewglobusproject.R // Make sure to import your R file

class HomeAdapter(
    private var productList: MutableList<Product>,
    private val onItemClick: (Product) -> Unit // Click listener
) : RecyclerView.Adapter<HomeAdapter.ProductViewHolder>() { // Reference to HomeAdapter instead of ProductAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "${product.price} сум"

        // Load images using Glide
        if (product.images.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(product.images[0].image)
                .into(holder.imageView)
        }

        // Set up the click listener
        holder.itemView.setOnClickListener {
            onItemClick(product) // Pass the current product to the listener
        }
    }

    // Function to add new products to the list
    fun addProducts(newProducts: List<Product>) {
        productList.addAll(newProducts)
        notifyDataSetChanged()
    }


    // for ViewProductActivity
    fun updateProducts(newProducts: List<Product>) {
        productList.clear()
        productList.addAll(newProducts)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}

