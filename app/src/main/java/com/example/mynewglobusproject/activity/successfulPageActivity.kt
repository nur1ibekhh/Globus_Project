package com.example.mynewglobusproject.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.fragment.Home

class successfulPageActivity : AppCompatActivity() {

    private lateinit var backToHomeBtn:Button
    private lateinit var openHistoryPageTxt:TextView
    private lateinit var userNameTxt:TextView


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_page)
        backToHomeBtn = findViewById(R.id.backToHomeBtn)
        openHistoryPageTxt = findViewById(R.id.openHistoryPageTxt)
        userNameTxt = findViewById(R.id.userNameTxt)



        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("user_id", -1)
        val firstName = sharedPreferences.getString("first_name", null)
        val lastName = sharedPreferences.getString("last_name", null)
        val phone = sharedPreferences.getString("phone", null)
        val dateOfBirth = sharedPreferences.getString("date_of_birth", null)
        val gender = sharedPreferences.getString("gender", null)
        val isActive = sharedPreferences.getBoolean("is_active", false)

        userNameTxt.text = (firstName.toString() +" "+ lastName.toString())

        backToHomeBtn.setOnClickListener { startActivity(Intent(this,MainActivity::class.java)) }
        openHistoryPageTxt.setOnClickListener { startActivity(Intent(this,MyOrderHistoryActivity::class.java)) }
    }

}