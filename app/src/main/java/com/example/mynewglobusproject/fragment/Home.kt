package com.example.mynewglobusproject.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewglobusproject.ProductsAdapter
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.activity.DetailsActivity
import com.example.mynewglobusproject.activity.ViewProductActivity
import com.example.mynewglobusproject.adapter.BannerAdapter
import com.example.mynewglobusproject.model.HomeProducts
import com.example.mynewglobusproject.model.Product
import com.example.mynewglobusproject.network.RetrofitInstance
import com.example.mynewglobusproject.utils.ApiClient.apiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Home page is all sale product and interest product for users

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var viewPagerSlider: RecyclerView
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var progressBarBanner: ProgressBar
    private lateinit var searchView: androidx.appcompat.widget.SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.viewRecomendion)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        progressBar = view.findViewById(R.id.progressBarBestseller)
        progressBarBanner = view.findViewById(R.id.progressBarBanner)

        viewPagerSlider = view.findViewById(R.id.viewPagerSlider)
        viewPagerSlider.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val seeAllTxv = view.findViewById<TextView>(R.id.seeAllTxv)

        seeAllTxv.setOnClickListener {
            callViewProductsAll()
        }

        searchView = view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchViewHome)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchProducts(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optional: live search
                return false
            }
        })

        fetchDiscountedProducts()
        fetchHomeProducts()

        return view
    }

    private fun searchProducts(query: String) {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val response = apiService.searchProducts(query)
                if (response.isSuccessful) {
                    val products = response.body()?.data?.items
                    if (products != null) {
                        val adapter = ProductsAdapter(products) { product ->
                            navigateToDetails(product)
                        }
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("SearchError", "Response code: ${response.code()}")
                    Toast.makeText(requireContext(), "No products found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("SearchError", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Failed to fetch products", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val firstName = sharedPreferences.getString("first_name", null)

        val txtFirstName = view.findViewById<TextView>(R.id.txtFirstName)
        txtFirstName.text = firstName ?: getString(R.string.default_name)

        val notificationIcon: ImageView = view.findViewById(R.id.iv_notification)
        notificationIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, NotificationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fetchDiscountedProducts() {
        progressBarBanner.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = apiService.getDiscountedProducts()
                if (response.isSuccessful) {
                    val products = response.body()?.data?.items
                    products?.let {
                        bannerAdapter = BannerAdapter(it, requireContext())
                        viewPagerSlider.adapter = bannerAdapter
                    }
                } else {
                    Log.e("HomeFragment", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error fetching products", e)
            } finally {
                progressBarBanner.visibility = View.GONE
            }
        }
    }

    private fun callViewProductsAll() {
        val intent = Intent(requireContext(), ViewProductActivity::class.java)
        startActivity(intent)
    }

    private fun fetchHomeProducts() {
        progressBar.visibility = View.VISIBLE
        RetrofitInstance.api.getHomeProducts().enqueue(object : Callback<HomeProducts> {
            override fun onResponse(call: Call<HomeProducts>, response: Response<HomeProducts>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.data?.items?.let { products ->
                        val adapter = ProductsAdapter(products) { product ->
                            navigateToDetails(product)
                        }
                        recyclerView.adapter = adapter
                    }
                } else {
                    handleApiError(response.code())
                }
            }

            override fun onFailure(call: Call<HomeProducts>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("API Error", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToDetails(product: Product) {
        val intent = Intent(requireContext(), DetailsActivity::class.java).apply {
            putExtra("PRODUCT_NAME", product.name)
            putExtra("PRODUCT_DESCRIPTION", product.description)
            putExtra("PRODUCT_PRICE", product.price)
            putExtra("PRODUCT_IMAGE", product.images.firstOrNull()?.image)
            putExtra("PRODUCT_ID", product.id)
            putExtra("PRODUCT_category", product.category)
        }
        startActivity(intent)
    }

    private fun handleApiError(code: Int) {
        Log.e("API Error", "Response code: $code")
        Toast.makeText(requireContext(), "Error: $code", Toast.LENGTH_SHORT).show()
    }
}




