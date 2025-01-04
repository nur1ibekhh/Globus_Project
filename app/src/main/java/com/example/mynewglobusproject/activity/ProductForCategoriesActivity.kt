package com.example.mynewglobusproject.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.adapter.HomeAdapter
import com.example.mynewglobusproject.adapter.ProductCategorysAdapter
import com.example.mynewglobusproject.model.ApiResponse
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.network.RetrofitInstance.retrofit
import com.example.mynewglobusproject.utils.ApiClient
import com.example.mynewglobusproject.utils.GlobusMarketUtils.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*
Categorys after set on views products for category
Its get categoryId after category page ******
 */

class ProductForCategoriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: HomeAdapter
    private lateinit var bacBtn: ImageView
    private var currentOffset: Int = 0 // Current offset
    private lateinit var searchView: androidx.appcompat.widget.SearchView



    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_detail_products)

        recyclerView = findViewById(R.id.recyclerView)
        bacBtn = findViewById(R.id.bacMainFromView)
        searchView = findViewById(R.id.searchViewProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Create the adapter with click listener
        productAdapter = HomeAdapter(mutableListOf()) { product ->
            // Handle item click and start DetailsActivity
            val intent = Intent(this, DetailsActivity::class.java).apply {
                putExtra("PRODUCT_ID", product.id) // Send product ID
                putExtra("PRODUCT_NAME", product.name) // Send product name
                putExtra("PRODUCT_DESCRIPTION", product.description) // Send product description
                putExtra("PRODUCT_PRICE", product.price) // Send product price
                putExtra("PRODUCT_CATEGORY", product.category) // Send product category
                putExtra("PRODUCT_IMAGE", product.images[0].image) // Send product image URL
                Log.d("ProductForCategories", "View: ${product.category}")
            }
            startActivity(intent)
        }

        recyclerView.adapter = productAdapter

        bacBtn.setOnClickListener { finish() }

        setupSearchView()


        // Intent orqali kategoriya ID sini olish
      val  categoryId = intent.getIntExtra("categoryId", 0) // Kategoriya ID ni olish
        Log.d("ProductForCategoriesActivity", "Category ID: $categoryId")

        fetchProducts(categoryId, currentOffset) // Fetch products by category
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
                    Toast.makeText(this@ProductForCategoriesActivity, "Search failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Search Error", t.message ?: "Error occurred")
                Toast.makeText(this@ProductForCategoriesActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchProducts(category: Int, offset: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyCategoryApi::class.java)
        val call = apiService.getProducts(category, offset)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.data?.items ?: emptyList()

                    // Add new products to the adapter
                    productAdapter.addProducts(products)

                    // If there are more pages, fetch the next page
                    if (products.isNotEmpty()) {
                        currentOffset += 20 // Update the offset
                        fetchProducts(category, currentOffset) // Call next page
                    }
                } else {
                    Toast.makeText(this@ProductForCategoriesActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API Error", t.message ?: "Error occurred")
                Toast.makeText(this@ProductForCategoriesActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}







