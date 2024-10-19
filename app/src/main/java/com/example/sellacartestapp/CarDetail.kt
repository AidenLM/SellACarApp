package com.example.sellacartestapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sellacartestapp.databinding.ActivityCarDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CarDetail : AppCompatActivity() {
    private lateinit var binding: ActivityCarDetailBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUse

         */

        // Initialize FragmentManager here
        fragmentManager = supportFragmentManager

        val uploaderUid = intent.getStringExtra("uploaderUid")
        if (uploaderUid.isNullOrEmpty()) {
            Toast.makeText(this, "Uploader UID not found", Toast.LENGTH_LONG).show()
        } else {
            fetchUploaderDetails(uploaderUid)
        }



        val brand = intent.getStringExtra("carBrand")
        val model = intent.getStringExtra("carModel")
        val year = intent.getStringExtra("carYear")
        val date = intent.getStringExtra("date")
        val kilometer = intent.getStringExtra("carKilometer")
        val price = intent.getStringExtra("carPrice")
        val comment = intent.getStringExtra("carComment")
        val imageUrl = intent.getStringExtra("carImageUrl")
        val location = intent.getStringExtra("carLocation")
        val descriptionText  = intent.getStringExtra("descriptionText")


        // Pass data to fragment
        val carFeaturesFragment = CarFeaturesFragment().apply {
            arguments = Bundle().apply {
                putString("carBrand", brand)
                putString("carModel", model)
                putString("carYear", year)
                putString("date",date)
                putString("carKilometer", kilometer)
                putString("carPrice", price)
                putString("carComment", comment)
                putString("carImageUrl", imageUrl)
                putString("carLocation", location)
            }
        }

        val carDescriptionFragment = CarDescriptionFragment().apply {
            arguments = Bundle().apply {
                putString("descriptionText",descriptionText)
            }


        }




        // Show the CarFeaturesFragment by default
        openFragment(carFeaturesFragment)


        binding.fragmentFeaturesButton.setOnClickListener {
            openFragment(carFeaturesFragment)
        }
        // Handle button click to switch to CarDescriptionFragment
        binding.fragmentDescriptionButton.setOnClickListener {
            openFragment(carDescriptionFragment)
        }




        //set location
        if(location != null){
            binding.textViewOnImage2.text = location
        }else{
            binding.textViewOnImage2.text = "Unknown"
        }

        binding.arrowDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        /*
        binding.brandTextView.text = brand
        binding.modelTextView.text = model
        binding.yearTextView.text = year
        binding.kilometerTextView.text = kilometer
        binding.priceTextView.text = price
        binding.commentTextView.text = comment
         */


        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(binding.imageView4)
        } else {
            // Fotoğraf URL'si yoksa varsayılan bir resim yükle
            binding.imageView4.setImageResource(R.drawable.ic_launcher_background)
        }
    }


    private fun openFragment(fragment: Fragment) {
        // Ensure fragmentManager is initialized
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun fetchUploaderDetails(uploaderUid: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(uploaderUid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("name") ?: "Unknown"
                    val surname = document.getString("surname") ?: "Unknown"
                    binding.textViewOnImage1.text = "$name $surname" // Display the uploader's name and surname
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching user details: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }




    /*
    private fun openFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

     */




}





