// ProductAdapter.kt
package com.example.mynewglobusproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.mynewglobusproject.model.Product
import com.squareup.picasso.Picasso

class ProductsAdapter(
    private val productList: List<Product>,
    private val onItemClick: (Product) -> Unit // Pass a click listener
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)

        fun bind(item: Product, onItemClick: (Product) -> Unit) {
            productName.text = item.name
            productPrice.text = "${item.price} UZS"

            if (item.images.isNotEmpty()) {
                Picasso.get()
                    .load(item.images[0].image)
                    .placeholder(R.drawable.shopping_icon)
                    .error(R.drawable.red_circle)
                    .into(productImage)
            }

            // Set click listener
            itemView.setOnClickListener {
                onItemClick(item) // Call the click listener with the product
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_best_seller, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position], onItemClick)
    }

    override fun getItemCount(): Int = productList.size
}




