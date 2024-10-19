package com.example.sellacartestapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.sellacartestapp.databinding.FragmentCarFeatures2Binding


class CarFeaturesFragment : Fragment() {
    private lateinit var binding: FragmentCarFeatures2Binding
    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCarFeatures2Binding.inflate(inflater,container,false)
        val view = binding.root


        val brand = arguments?.getString("carBrand")
        val model = arguments?.getString("carModel")
        val year = arguments?.getString("carYear")
        val date = arguments?.getString("date")
        val kilometer = arguments?.getString("carKilometer")
        val price = arguments?.getString("carPrice")

        /*
        val comment = arguments?.getString("carComment")
        val imageUrl = arguments?.getString("carImageUrl")
        val email = arguments?.getString("userEmail")
        val location = arguments?.getString("carLocation")

         */

        // Use the retrieved data (e.g., set text on TextViews)
        binding.brandEditable.text = brand
        binding.modelEditable.text = model
        binding.yearEditable.text = year
        binding.dateEditable.text = date
        binding.kilometerEditable.text = kilometer
        binding.priceEditable.text = price




        return view
    }


}