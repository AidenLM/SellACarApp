package com.example.sellacartestapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener

import com.example.sellacartestapp.databinding.FragmentAddPostBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonCancellable.parent
import java.util.Calendar
import java.util.UUID
import java.util.jar.Manifest

class AddPostFragment : Fragment() {
    private var selectedKilometerUnit: String = "KM"
    private var selectedPriceUnit: String = "TL"
    private lateinit var binding: FragmentAddPostBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    var selectedPicture: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ViewBinding kullanarak layout'u inflate ediyoruz
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = binding.root

        // ViewBinding üzerinden spinner'lara erişiyoruz
        val brandSpinner = binding.addPostBrandSpinner
        val modelSpinner = binding.addPostModelSpinner

        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedBrand = parent?.getItemAtPosition(position).toString()
                updateModelSpinner(selectedBrand)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
        }

        // Marka spinner'ı için adapter'i oluşturuyoruz
        setupBrandSpinner()
        setupYearSpinner()
        setupLocationSpinner()
        setupKilometerUnitSpinner()
        setupPriceUnitSpinner()

        registerLauncher()

        binding.imaageView4.setOnClickListener {
            addImage(it)
        }

        binding.button.setOnClickListener {
            addPost(it)


            // Show the RecyclerView in MainActivity
            (activity as? MainActivity)?.showRecyclerView()
        }

        return view
    }


    private fun setupKilometerUnitSpinner() {

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.kilometer_units_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.kilometerUnitSpinner.adapter = adapter
        binding.kilometerUnitSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedKilometerUnit = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }


    private fun setupPriceUnitSpinner() {

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.price_units_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.priceUnitSpinner.adapter = adapter
        binding.priceUnitSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedPriceUnit = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }


    }


    private fun setupBrandSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.brands,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.addPostBrandSpinner.adapter = adapter
    }

    private fun setupLocationSpinner() {

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Locations,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.addPostCitySpinner.adapter = adapter
    }

    private fun setupYearSpinner() {
        // Get the current year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        // Generate a list of years from 1800 to the current year
        val yearsList = (1800..currentYear).toList().reversed()
        // Create an ArrayAdapter using the list of years
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            yearsList
        )
        // Set the adapter for the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.addPostYearSpinner.adapter = adapter

    }

    private fun updateModelSpinner(brand: String) {
        val modelArrayResId = when (brand) {
            "Mercedes-Benz" -> R.array.mercedes_models
            "BMW" -> R.array.bmw_models
            "Mazda" -> R.array.mazda_models
            "Audi" -> R.array.audi_models
            "Toyota" -> R.array.toyota_models
            "Ford" -> R.array.ford_models
            "Honda" -> R.array.honda_models
            "Nissan" -> R.array.nissan_models
            "Volkswagen" -> R.array.volkswagen_models
            "Chevrolet" -> R.array.chevrolet_models
            "Hyundai" -> R.array.hyundai_models
            "Jaguar" -> R.array.jaguar_models
            "Land Rover" -> R.array.land_rover_models
            "Kia" -> R.array.kia_models
            "Jeep" -> R.array.jeep_models
            "Subaru" -> R.array.subaru_models
            "Volvo" -> R.array.volvo_models
            "Porsche" -> R.array.porsche_models
            "Ferrari" -> R.array.ferrari_models
            "Lamborghini" -> R.array.lamborghini_models
            "Peugeot" -> R.array.peugeot_models
            "Renault" -> R.array.renault_models
            else -> R.array.models // Default if no brand matches
        }
        val modelAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            modelArrayResId,
            android.R.layout.simple_spinner_item
        )
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.addPostModelSpinner.adapter = modelAdapter
    }

    private fun addPost(view: View) {
        // universal unique id
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("Cars").child(imageName)

        if (selectedPicture != null) {
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                // Download url -> firestore
                val uploadPictureReference = storage.reference.child("Cars").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()

                    // Retrieve values from components
                    val comment = binding.commentEditText.text.toString()
                    val carBrand = binding.addPostBrandSpinner.selectedItem.toString()
                    val carModel = binding.addPostModelSpinner.selectedItem.toString()
                    val carYear = binding.addPostYearSpinner.selectedItem.toString()
                    val carLocation = binding.addPostCitySpinner.selectedItem.toString()
                    var carPrice = binding.priceEditText.text.toString()
                    var carKilometer = binding.kilometerEditText.text.toString()
                    val descriptionText = binding.addPostDescription.text.toString()

                    carKilometer = "$carKilometer $selectedKilometerUnit"
                    carPrice = "$carPrice $selectedPriceUnit"

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val userEmail = currentUser?.email ?: "Unknown User"

                    val userUid = currentUser?.uid ?: "Unknown UID" // Get UID

                    // Create a map to store car details
                    val carMap = hashMapOf<String, Any>(
                        "comment" to comment,
                        "downloadUrl" to downloadUrl,
                        "carBrand" to carBrand,
                        "carModel" to carModel,
                        "carYear" to carYear,
                        "carLocation" to carLocation,
                        "carPrice" to carPrice,
                        "carKilometer" to carKilometer,
                        "date" to Timestamp.now(),
                        "userEmail" to userEmail,
                        "uploaderUid" to userUid,
                        "descriptionText" to descriptionText
                    )

                    // Upload car data to Firestore
                    firestore.collection("Cars").add(carMap).addOnSuccessListener {
                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.remove(this)
                        fragmentTransaction.commit()
                        Toast.makeText(context, "Post uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun addImage(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    Snackbar.make(view, "Galeri İçin İzin Gerekiyor!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Erişime İzin Ver") {
                            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                        }.show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intentToGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Çoklu seçim
                activityResultLauncher.launch(intentToGallery)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    Snackbar.make(view, "Galeri İçin İzin Gerekiyor!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Erişime İzin Ver") {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }.show()
                } else {
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intentToGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Çoklu seçim
                activityResultLauncher.launch(intentToGallery)
            }
        }
    }


    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let {
                            binding.imaageView4.setImageURI(it)
                        }
                    }
                }

            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //permission granted
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    //permission denied
                    Toast.makeText(requireContext(), "İzin Gerekli!", Toast.LENGTH_LONG).show()
                }
            }
    }
}
