package com.example.mynewglobusproject.fragment

// MyOrder = Cart fragment for add user product


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.PayMe.PayMePayoutActivity
import com.example.mynewglobusproject.activity.CanfromInfoActivity
import com.example.mynewglobusproject.adapter.CartAdapter
import com.example.mynewglobusproject.model.CartItem
import com.example.mynewglobusproject.utils.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyOrder : BaseFragment() {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var cartAdapter: CartAdapter
    private val cartItems = mutableListOf<CartItem>()
    private lateinit var checkOutBtn: Button
    private lateinit var deleteAllProduct: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_order, container, false)

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)
        progressBar = view.findViewById(R.id.progressBar)

        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        cartAdapter = CartAdapter(requireContext(), cartItems, getAccessToken()!!)
        recyclerViewCart.adapter = cartAdapter

        checkOutBtn = view.findViewById(R.id.checkOutBtn)


        deleteAllProduct = view.findViewById(R.id.deleteAllProduct)
        deleteAllProduct.setOnClickListener { deleteAllCartItems() }


        fetchCartData()
        return view
    }

    private fun calculateTotalPrice() {
        // Total narxni hisoblash
        val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

        checkOutBtn.setOnClickListener {
            // Intent yaratish va totalPrice ni yuborish
            val intent = Intent(requireContext(), CanfromInfoActivity::class.java)
            intent.putExtra("totalPrice", totalPrice)
            startActivity(intent)
        }
    }

    private fun deleteAllCartItems() {
        val token = getAccessToken()
        calculateTotalPrice()
        if (token == null) {
            Toast.makeText(requireContext(), "No access token found", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.deleteAllCartItems("Bearer $token")
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        cartItems.clear()
                        cartAdapter.notifyDataSetChanged()
                        calculateTotalPrice()  // Narxni qayta hisoblash
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete items", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchCartData() {
        progressBar.visibility = View.VISIBLE

        val token = getAccessToken()
        calculateTotalPrice()
        if (token == null) {
            progressBar.visibility = View.GONE
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.getCartItems("Bearer $token")
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.data.cart.isNotEmpty()) {
                        cartItems.clear()
                        cartItems.addAll(response.data.cart)  // data.cart ni qo'shish
                        cartAdapter.notifyDataSetChanged()

                        // Narxni hisoblash
                        calculateTotalPrice()
                    } else {
                        Toast.makeText(requireContext(), "No items found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}











