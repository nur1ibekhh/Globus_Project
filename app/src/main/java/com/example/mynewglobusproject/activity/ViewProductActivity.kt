package com.example.mynewglobusproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.adapter.HomeAdapter
import com.example.mynewglobusproject.model.ApiResponse
import com.example.mynewglobusproject.model.Product
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.utils.GlobusMarketUtils.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewProductActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: HomeAdapter
    private lateinit var bacBtn: ImageView
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private var currentOffset: Int = 0 // Current offset

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_detail_products)

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        bacBtn = findViewById(R.id.bacMainFromView)
        searchView = findViewById(R.id.searchViewProducts)

        // Set RecyclerView layout manager
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Create and set the adapter
        productAdapter = HomeAdapter(mutableListOf()) { product ->
            // Handle item click and navigate to DetailsActivity
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id) // Send product ID
                putExtra("PRODUCT_NAME", product.name) // Send product name
                putExtra("PRODUCT_DESCRIPTION", product.description) // Send product description
                putExtra("PRODUCT_PRICE", product.price) // Send product price
                putExtra("PRODUCT_CATEGORY", product.category) // Send product category
                putExtra("PRODUCT_IMAGE", product.images[0].image) // Send product image URL
            }
            startActivity(intent)
        }
        recyclerView.adapter = productAdapter

        // Back button click listener
        bacBtn.setOnClickListener { finish() }

        // Set up SearchView for search functionality
        setupSearchView()

        // Fetch initial products
        fetchProducts(currentOffset)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchDetails(it) // Perform search
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: Handle real-time search filtering
                return false
            }
        })
    }

    private fun searchDetails(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyCategoryApi::class.java)
        val call = apiService.searchProductss(query)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.data?.items ?: emptyList()
                    productAdapter.updateProducts(products) // Update adapter with search results
                } else {
                    Toast.makeText(this@ViewProductActivity, "Search failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Search Error", t.message ?: "Error occurred")
                Toast.makeText(this@ViewProductActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchProducts(offset: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyCategoryApi::class.java)
        val call = apiService.getProducts(offset)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.data?.items ?: emptyList()
                    productAdapter.addProducts(products) // Add fetched products to adapter

                    if (products.isNotEmpty()) {
                        currentOffset += 20 // Update offset for next fetch
                        fetchProducts(currentOffset) // Fetch next page
                    }
                } else {
                    Toast.makeText(this@ViewProductActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API Error", t.message ?: "Error occurred")
                Toast.makeText(this@ViewProductActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



