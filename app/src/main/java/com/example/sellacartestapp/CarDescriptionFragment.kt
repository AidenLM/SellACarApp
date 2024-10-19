package com.example.sellacartestapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.sellacartestapp.databinding.FragmentCarDescriptionBinding


class CarDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentCarDescriptionBinding
    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCarDescriptionBinding.inflate(inflater,container,false)
        val view = binding.root


        val descriptionText = arguments?.getString("descriptionText")

        binding.carDescriptionTextView.text = descriptionText

        return view
    }


}