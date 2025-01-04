package com.example.mynewglobusproject.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.activity.ProductForCategoriesActivity
import com.example.mynewglobusproject.adapter.CategoryAdapter
import com.example.mynewglobusproject.model.Category
import com.example.mynewglobusproject.model.CategoryResponse
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.utils.GlobusMarketUtils.BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Its favourite = categories page , is all products add to categories detail

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Favourite : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var progressBarCategory: ProgressBar

    private val TAG: String = "Check_Response"

    private lateinit var categoryList: ArrayList<Category>
    private lateinit var productAdapter: CategoryAdapter
    private lateinit var recyclerviewCategory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Initialize the category list
        categoryList = ArrayList()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        // Find ProgressBar
        progressBarCategory = view.findViewById(R.id.progressBarCategorys)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find RecyclerView and set it up
        recyclerviewCategory = view.findViewById(R.id.recyclerview_category)
        recyclerviewCategory.setHasFixedSize(true)
        recyclerviewCategory.layoutManager = LinearLayoutManager(requireContext())

        // Fetch categories from the API
        getAllCategory()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Favourite().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun getAllCategory() {
        progressBarCategory.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyCategoryApi::class.java)

        api.getCategorys().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                progressBarCategory.visibility = View.GONE

                if (response.isSuccessful) {
                    response.body()?.let { categoryResponse ->
                        val categories = categoryResponse.data.categories
                        categoryList.addAll(categories)
                        Log.d(TAG, "Fetched categories: $categories")

                        // Adapterni ClickListener bilan sozlash
                        productAdapter = CategoryAdapter(categoryList) { category ->
                            // Intent orqali ProductForCategoriesActivity ni ochish
                            val intent = Intent(requireContext(), ProductForCategoriesActivity::class.java)
                            intent.putExtra("categoryId", category.id) // Kategoriya ID ni yuborish
                            startActivity(intent)
                        }
                        recyclerviewCategory.adapter = productAdapter
                    }
                } else {
                    Log.d(TAG, "Error: ${response.code()}")
                    Log.d(TAG, "Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                progressBarCategory.visibility = View.GONE
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

}

