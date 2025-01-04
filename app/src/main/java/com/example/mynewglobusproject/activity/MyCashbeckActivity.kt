package com.example.mynewglobusproject.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.model.CashbackResponse
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.network.RetrofitClient
import com.example.mynewglobusproject.network.RetrofitInstance
import com.example.mynewglobusproject.utils.ApiClient
import retrofit2.Call

class MyCashbeckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cashbeck)

        val token = getAccessToken()
        if (token != null) {
            fetchCashback("Bearer $token")
        } else {
            Toast.makeText(this, "Access token not found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }

    private fun fetchCashback(authToken: String) {
        val call = RetrofitInstance.api.getCashback(authToken)

        call.enqueue(object : retrofit2.Callback<CashbackResponse> {
            override fun onResponse(
                call: Call<CashbackResponse>,
                response: retrofit2.Response<CashbackResponse>
            ) {
                if (response.isSuccessful) {
                    val cashback = response.body()?.cashback ?: 0.0
                    displayCashback(cashback)
                } else {
                    Toast.makeText(
                        this@MyCashbeckActivity,
                        "Failed to fetch cashback",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<CashbackResponse>, t: Throwable) {
                Toast.makeText(
                    this@MyCashbeckActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun displayCashback(cashback: Double) {
        val cashbackTextView = findViewById<TextView>(R.id.cashbeckTxv)
        cashbackTextView.text = "$cashback UZS"
    }
}
