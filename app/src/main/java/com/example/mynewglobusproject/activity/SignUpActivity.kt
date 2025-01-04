package com.example.mynewglobusproject.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.model.ApiResponse
import com.example.mynewglobusproject.model.User
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.network.RetrofitClient
import com.example.mynewglobusproject.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * In SignUpActivity, user can login using full name, password
 */

class SignUpActivity : BaseActivity() {

    val TAG = SignInActivity::class.java.toString()

    lateinit var et_fullName: EditText
    lateinit var et_password: EditText
    lateinit var et_cpassword:EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        // views
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)

        // Formatters
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy MMM dd")

        // Variables to store selected date
        var selectedDate = LocalDate.now()


        btnPickDate.setOnClickListener {
            val today = Calendar.getInstance()
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Yil-Oy-Kun formatida

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth) // Oyni 1 ga qo'shish
                    tvSelectedDate.text = dateFormatter.format(selectedDate) // Yangi format bilan sanani ko'rsatish
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = today.timeInMillis // Hozirgi kun va undan oldingi sanalar
            datePickerDialog.show()
        }



        btnRegister.setOnClickListener {
            val user = User(
                id = 25,
                first_name = etFirstName.text.toString(),
                last_name = etLastName.text.toString(),
                password = etPassword.text.toString(),
                phone = etPhone.text.toString(),
                date_of_birth = tvSelectedDate.text.toString(), // YYYY-MM-DD formatida
                gender = "male" // yoki "female"
            )

            if (user.first_name.isNotEmpty() && user.phone.isNotEmpty() && user.password.isNotEmpty() && user.date_of_birth!!.isNotEmpty()) {
               registerUser(user)
                Log.d("sign","$user")
            } else {
                Toast.makeText(this, "Barcha maydonlarni to‘ldiring", Toast.LENGTH_SHORT).show()
            }

        }

        val tv_signin = findViewById<TextView>(R.id.tv_signin)

        tv_signin.setOnClickListener { callSignInActivity() }

        initViews()
    }

    private fun registerUser(user: User) {
        // Bu yerda server URL ning to'g'ri ekanligini tekshirib ko'ring
        val call = RetrofitInstance.api.registerUser(user)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        // Ro'yxatdan o'tish muvaffaqiyatli bo'ldi
                        val registeredUser = apiResponse.data
                        Log.d("sign", "userss: $registeredUser")
                        // Success OTP Activity-ga o'tish
                        callSuccessOTPActivity()
                    } else {
                        val errorMessage = apiResponse?.errMessage ?: "Ro'yxatdan o'tish muvaffaqiyatsiz"
                        Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Agar server 500 xato qaytarsa
                    Toast.makeText(this@SignUpActivity, "Server xatosi: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("Errorss", "Response code: ${response.code()}") // xatolik kodi
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Agar tarmoqda xato bo'lsa
                Toast.makeText(this@SignUpActivity, "Xato: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Errorss", "Failure: ${t.message}") // xatolik haqida ma'lumot
            }
        })
    }




    private fun initViews() {
    }


    private fun callMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun callSuccessOTPActivity() {
        val intent = Intent(this, SucsesOTPActivity::class.java)
        startActivity(intent)
    }

    private fun callSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}