package com.example.mynewglobusproject.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewglobusproject.model.VerifyRequest
import com.example.mynewglobusproject.network.MyCategoryApi
import com.example.mynewglobusproject.utils.GlobusMarketUtils.BASE_URL
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VerifyViewModel : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyCategoryApi::class.java)

    val verificationResult: MutableLiveData<String> = MutableLiveData()

    fun verifyOtp(phone: String, otp: String) {
        val request = VerifyRequest(phone, otp)
        viewModelScope.launch {
            try {
                val response = api.verifyOtp(request)
                if (response.isSuccessful && response.body() != null) {
                    verificationResult.value = response.body()?.message ?: "Verification successful"
                } else {
                    verificationResult.value = "Error: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                verificationResult.value = "Exception: ${e.localizedMessage}"
            }
        }
    }
}
