package com.example.mynewglobusproject.fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.activity.AboutShopActivity
import com.example.mynewglobusproject.activity.ChangeProfileActivity
import com.example.mynewglobusproject.activity.MyCashbeckActivity
import com.example.mynewglobusproject.activity.MyOrderHistoryActivity
import com.example.mynewglobusproject.activity.SignInActivity
import com.example.mynewglobusproject.databinding.FragmentProfileBinding


// its profile page for user , is market about .....
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Profile : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Accessing SharedPreferences in a Fragment
        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("user_id", -1)
        val firstName = sharedPreferences.getString("first_name", null)
        val lastName = sharedPreferences.getString("last_name", null)
        val phone = sharedPreferences.getString("phone", null)
        val dateOfBirth = sharedPreferences.getString("date_of_birth", null)
        val gender = sharedPreferences.getString("gender", null)
        val isActive = sharedPreferences.getBoolean("is_active", false)

        // Use the retrieved data (for example, display it in TextViews)
        val txtFirstName = view.findViewById<TextView>(R.id.txtFirstName)
        val txtPhone = view.findViewById<TextView>(R.id.txtPhone)

        txtFirstName.text = firstName ?: "N/A"
        txtPhone.text = phone ?: "N/A"

        // Handle other data similarly...
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Release the binding to avoid memory leaks
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setupListeners() {

        binding.changeProfile.setOnClickListener {
            startActivity(Intent(requireContext(), ChangeProfileActivity::class.java))
        }

        binding.aboutShop.setOnClickListener {
            startActivity(Intent(requireContext(), AboutShopActivity::class.java))
        }

        binding.cashbackBtn.setOnClickListener{
            startActivity(Intent(requireContext(),MyCashbeckActivity::class.java))
        }

        binding.myOrderHistory.setOnClickListener {
            startActivity(Intent(requireContext(),MyOrderHistoryActivity::class.java))
        }

        binding.logoutLayout.setOnClickListener {
            // Start SignInActivity and clear the back stack to prevent going back to the Profile fragment
        }

    }
}
