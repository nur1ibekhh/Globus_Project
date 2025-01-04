package com.example.mynewglobusproject.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mynewglobusproject.R
import com.example.mynewglobusproject.databinding.ActivityMainBinding
import com.example.mynewglobusproject.fragment.Favourite
import com.example.mynewglobusproject.fragment.Home
import com.example.mynewglobusproject.fragment.MyOrder
import com.example.mynewglobusproject.fragment.Profile

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(Home())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.profile -> replaceFragment(Profile())
                R.id.favourite -> replaceFragment(Favourite())
                R.id.my_order -> replaceFragment(MyOrder())

                else ->{

                }

            }

            true

        }


    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}