package com.example.mynewglobusproject.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mynewglobusproject.PayMe.PayMePayoutActivity
import com.example.mynewglobusproject.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class CanfromInfoActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var etAddress: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canfrom_info)

        // Tugmalarni ID orqali topamiz
        val backCartView = findViewById<ImageView>(R.id.backCartView)

        val userNameTxt = findViewById<EditText>(R.id.userNameEtx)
        val userPhoneTxt = findViewById<EditText>(R.id.userPhoneEtx)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("user_id", -1)
        val firstName = sharedPreferences.getString("first_name", null)
        val lastName = sharedPreferences.getString("last_name", null)
        val phone = sharedPreferences.getString("phone", null)
        val dateOfBirth = sharedPreferences.getString("date_of_birth", null)
        val gender = sharedPreferences.getString("gender", null)
        val isActive = sharedPreferences.getBoolean("is_active", false)

       // EditText'ga matnni o'rnatish
        userNameTxt.setText(("$firstName  $lastName") ?: "No first name")
        userPhoneTxt.setText(phone ?: "No phone number")

        // Intent orqali yuborilgan totalPrice ni olish
        val totalPrice = intent.getIntExtra("totalPrice", 0)

        // Total narxni ekranda ko'rsatish
        val totalFeeTxt = findViewById<TextView>(R.id.totalFeeTxt)
        val sumTotalPrice = findViewById<TextView>(R.id.sumTotalPrice)
        val salePriceTxt = findViewById<TextView>(R.id.salePriceTxt)
        val totalTxt = findViewById<TextView>(R.id.totalTxt)

        totalTxt.text = "$totalPrice"
        totalFeeTxt.text = "$totalPrice"


        etAddress = findViewById(R.id.etAddress)
        val btnGetAddress: Button = findViewById(R.id.btnGetAddress)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnGetAddress.setOnClickListener {
            getLocation()
        }



        // back to cart fragment
        backCartView.setOnClickListener { finish() }

        initViews()

    }

    private fun getLocation() {
        // Ruxsat tekshirish
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val address = addresses?.get(0)?.getAddressLine(0) // To‘liq manzil
                            etAddress.setText(address)
                        } else {
                            Toast.makeText(this, "Manzil topilmadi", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Joylashuv topilmadi", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Ruxsatni so'rash
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(this, "Ruxsat berilmagan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectPaymetMethod() {
        // Find the RadioGroup and Button
        val radioGroupPay = findViewById<RadioGroup>(R.id.radioGroupPay)
        val canfromOutBtn = findViewById<Button>(R.id.canfromOutBtn)

        // Set an OnClickListener for the button
        canfromOutBtn.setOnClickListener {
            // Get the selected RadioButton's ID
            val selectedId = radioGroupPay.checkedRadioButtonId

            if (selectedId != -1) {
                // Find the selected RadioButton
                val selectedRadioButton = findViewById<RadioButton>(selectedId)

                when (selectedId) {
                    R.id.radioButtonPay1 -> {
                        // Navigate to PayMePayoutActivity
                        val intent = Intent(this, PayMePayoutActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.radioButtonPay2 -> {
                        // Navigate to successfulPageActivity
                        val intent = Intent(this, successfulPageActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else {
                // Show a Toast if no option is selected
                Toast.makeText(this, "Please select a payment option", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        // Find the RadioGroup
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)

        // Set an OnCheckedChangeListener
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Find the selected RadioButton
            val selectedRadioButton = findViewById<RadioButton>(checkedId)

            // Get the text of the selected RadioButton
            val selectedText = selectedRadioButton.text.toString()

            // Show a Toast with the selected option
            Toast.makeText(this, "Selected: $selectedText", Toast.LENGTH_SHORT).show()

            // Perform additional actions based on the selection
            when (checkedId) {
                R.id.radioButtonOption1 -> {
                    selectPaymetMethod()
                    // Handle "Globus Maret shoxobchasida" selection
                }
                R.id.radioButtonOption2 -> {
                    selectPaymetMethod()
                    // Handle "Kurer orqali" selection
                }
            }
        }
    }
}

