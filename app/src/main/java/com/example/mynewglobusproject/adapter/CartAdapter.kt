package com.example.mynewglobusproject.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.activity.DetailsActivity
import com.example.mynewglobusproject.fragment.MyOrder
import com.example.mynewglobusproject.model.CartItem
import com.example.mynewglobusproject.model.CartUpdateRequest
import com.example.mynewglobusproject.utils.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<CartItem>,
    private val accessToken: String,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem, position)

    }

    override fun getItemCount(): Int = cartItems.size


    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productName)
        private val productQue: TextView = itemView.findViewById(R.id.productQue)
        private val productImageView: ImageView = itemView.findViewById(R.id.productImage)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.productPrice)
        private val minusCartBtn: ImageView = itemView.findViewById(R.id.minusCartBtn)
        private val plusCartBtn: TextView = itemView.findViewById(R.id.plusCartBtn)
        private val deletCartBtn: TextView = itemView.findViewById(R.id.deletCartBtn)


        fun bind(cartItem: CartItem, position: Int) {
            productNameTextView.text = cartItem.product.name
            productQue.text = cartItem.quantity.toString()
            productPriceTextView.text = "${cartItem.product.price} UZS"

            // Image loading with Glide or Picasso
            Glide.with(itemView.context)
                .load(cartItem.product.images?.get(0)?.image)
                .into(productImageView)

            // Handle minus button click
            minusCartBtn.setOnClickListener {
                deleteCartItem(cartItem.id, position)
            }

            // Handle plus button click
            plusCartBtn.setOnClickListener {
                val updatedQuantity = cartItem.quantity + 1
                updateCartItem(cartItem.id, cartItem.product.id, updatedQuantity, position)
            }

            // Handle delete button click
            deletCartBtn.setOnClickListener {
                val updatedQuantity = cartItem.quantity - 1
                updateCartItem(cartItem.id, cartItem.product.id, updatedQuantity, position)
            }


            // Open the product details activity
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java).apply {
                    putExtra("PRODUCT_NAME", cartItem.product.name)
                    putExtra("PRODUCT_DESCRIPTION", cartItem.product.description)
                    putExtra("PRODUCT_PRICE", cartItem.product.price)
                    putExtra("PRODUCT_IMAGE", cartItem.product.images?.get(0)?.image)
                    putExtra("PRODUCT_ID", cartItem.product.id)
                }
                context.startActivity(intent)
            }
        }

        private fun updateCartItem(
            cartItemId: Int,
            productId: Int,
            quantity: Int,
            position: Int
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiClient.apiService.updateCartItem(
                        "Bearer $accessToken",
                        cartItemId,
                        CartUpdateRequest(productId, quantity)
                    )
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            cartItems[position].quantity = quantity
                            notifyItemChanged(position)
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to update item: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun deleteCartItem(cartItemId: Int, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.deleteCartItem(
                    "Bearer $accessToken",
                    cartItemId
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        cartItems.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, cartItems.size)
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to remove item: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}














