package com.example.mynewglobusproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.activity.DetailsActivity
import com.example.mynewglobusproject.model.Product

class BannerAdapter(
    private val productList: List<Product>,
    private val context: Context
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val product = productList[position]

        // TextView'larni to'ldirish
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "${product.price} UZS"

        // Glide yordamida rasmni yuklash
        Glide.with(holder.itemView.context)
            .load(product.images.firstOrNull()?.image)
            .placeholder(R.drawable.shopping_icon) // Agar rasm bo'lmasa, zaxira tasvir
            .error(R.drawable.red_circle) // Rasm yuklashda xatolik yuz bersa
            .into(holder.imageView)

        // Element bosilganda DetailsActivity'ga o'tish
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("PRODUCT_NAME", product.name)
                putExtra("PRODUCT_DESCRIPTION", product.description)
                putExtra("PRODUCT_PRICE", product.price)
                putExtra("PRODUCT_IMAGE", product.images.firstOrNull()?.image)
                putExtra("PRODUCT_ID", product.id)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

    // ViewHolder klassi
    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.productName)
        val priceTextView: TextView = itemView.findViewById(R.id.productPrice)
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
    }
}



