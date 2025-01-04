package com.example.mynewglobusproject.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.model.UserProfile
import com.example.mynewglobusproject.model.UserProfileResponse
import com.example.mynewglobusproject.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ChangeProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        val id = sharedPreferences.getInt("user_id", -1)
        val isActive = sharedPreferences.getBoolean("is_active", false)

        val edtLastName = findViewById<EditText>(R.id.edtLastName)
        val edtFirstName = findViewById<EditText>(R.id.edtFirstName)
        val etDateOfBirth = findViewById<TextView>(R.id.etDateOfBirth)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val spGender = findViewById<Spinner>(R.id.spGender)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val backToProfilePage = findViewById<ImageView>(R.id.backToProfilePage)

        backToProfilePage.setOnClickListener { finish() }

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
                    etDateOfBirth.text = dateFormatter.format(selectedDate) // Yangi format bilan sanani ko'rsatish
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = today.timeInMillis // Hozirgi kun va undan oldingi sanalar
            datePickerDialog.show()
        }


        btnSave.setOnClickListener {
            // Get updated profile data from the fields
            val updatedProfile = UserProfile(
                id = id,
                first_name = edtFirstName.text.toString(),
                last_name = edtLastName.text.toString(),
                phone = etPhone.text.toString(),
                date_of_birth = etDateOfBirth.text.toString(),
                gender = spGender.selectedItem.toString()
            )

            val accessToken = getAccessToken()
            if (accessToken != null) {
                updateProfile(id, accessToken, updatedProfile)
                Log.d("ChangeProfileActivity", "$updatedProfile")
                finish()
            } else {
                Toast.makeText(this, "Access token not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }

    private fun updateProfile(userId: Int, token: String, profile: UserProfile) {
        val apiService = RetrofitInstance.api
        val call = apiService.updateUserProfile("Bearer $token", userId, profile)

        call.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()?.data?.user
                    userProfile?.let {
                        val message = """
                        Profile Updated:
                        Name: ${it.first_name} ${it.last_name}
                        Phone: ${it.phone}
                        DOB: ${it.date_of_birth}
                        Gender: ${it.gender}
                        Active: ${it.is_active}
                    """.trimIndent()
                        Toast.makeText(this@ChangeProfileActivity, "Profile o'zgarttirildi", Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ErrorResponse", "Code: ${response.code()}, Body: $errorBody")
                    Toast.makeText(this@ChangeProfileActivity, "Iltimos barcha maydonlarni to'ldiring", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(this@ChangeProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

