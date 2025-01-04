package com.example.mynewglobusproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.mynewglobusproject.R

class NotificationFragment : Fragment() {

    private lateinit var backToProfilePage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        backToProfilePage = view.findViewById(R.id.backToProfilePage)

        backToProfilePage.setOnClickListener {
            // Fragmentni orqaga qaytarish
            parentFragmentManager.popBackStack()
        }

        return view
    }



}
