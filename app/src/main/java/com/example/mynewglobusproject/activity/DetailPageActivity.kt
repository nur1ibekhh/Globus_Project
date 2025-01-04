package com.example.mynewglobusproject.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.adapter.ProductAdapter
import com.example.mynewglobusproject.model.ApiResponse
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.utils.GlobusMarketUtils.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailPageActivity:AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var currentOffset: Int = 0 // Hozirgi offset

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_detail_products)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        productAdapter = ProductAdapter(mutableListOf()) // Adapterni yaratish
        recyclerView.adapter = productAdapter

        // Intent orqali kelgan mahsulot ID’sini olish
        val productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            fetchProducts(productId, currentOffset) // Mahsulotlarni olish
        }
    }

    private fun fetchProducts(productId: Int, offset: Int) {
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

                    // Yangi mahsulotlarni adapterga qo'shish
                    productAdapter.addProducts(products)

                    // Agar sahifalar mavjud bo'lsa, keyingi sahifani olish
                    if (products.isNotEmpty()) {
                        currentOffset += 20 // Offsetni yangilash
                        fetchProducts(productId, currentOffset) // Keyingi sahifani chaqirish
                    }
                } else {
                    Toast.makeText(this@DetailPageActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API Error", t.message ?: "Error occurred")
                Toast.makeText(this@DetailPageActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}