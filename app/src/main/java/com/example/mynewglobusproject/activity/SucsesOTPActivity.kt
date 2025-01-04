package com.example.mynewglobusproject.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mynewglobusproject.R

import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mynewglobusproject.model.User
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SucsesOTPActivity : AppCompatActivity() {

    private lateinit var phoneTvText: TextView
    private lateinit var otpEditText: EditText
    private lateinit var verifyButton: Button
    private lateinit var user:User

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucses_otpactivity)

        phoneTvText = findViewById(R.id.phoneTvText)
        otpEditText = findViewById(R.id.otpEditText)
        verifyButton = findViewById(R.id.verifyButton)

        verifyButton.setOnClickListener {
            val phone = user.phone
            val otp = otpEditText.text.toString()

            if (phone.isNotEmpty() && otp.isNotEmpty()) {
                sendOtpVerification(phone, otp)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOtpVerification(phone: String, otp: String) {
        val apiService = RetrofitClient.instance.create(MyCategoryApi::class.java)
        val requestBody = HashMap<String, String>()
        requestBody["phone"] = phone
        requestBody["otp"] = otp

        apiService.verifyUser(requestBody).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SucsesOTPActivity, "Verification successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SucsesOTPActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SucsesOTPActivity, "Verification failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("Retrofit", "Error: ${t.message}")
                Toast.makeText(this@SucsesOTPActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
