package com.example.sellacartestapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellacartestapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var carArrayList: ArrayList<Car>
    private lateinit var fragmentManager: FragmentManager
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        //initialize
        auth = Firebase.auth
        db = Firebase.firestore
        carArrayList = ArrayList<Car>()

        // Initialize FragmentManager here
        fragmentManager = supportFragmentManager

        //TopBar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Toolbar
        setSupportActionBar(binding.include.myToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set RecyclerView Adapter
        // take data from adapter for click listener
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val carAdapter = MainActivityAdapter(carArrayList) { selectedCar ->
            val intent = Intent(this, CarDetail::class.java)
            intent.putExtra("carBrand", selectedCar.brand)
            intent.putExtra("carModel", selectedCar.model)
            intent.putExtra("carYear", selectedCar.year)
            intent.putExtra("date",selectedCar.date)
            intent.putExtra("carKilometer", selectedCar.kilometer)
            intent.putExtra("carPrice", selectedCar.price)
            intent.putExtra("carComment", selectedCar.comment)
            intent.putExtra("carImageUrl", selectedCar.downloadUrl)
            intent.putExtra("userEmail", selectedCar.email)
            intent.putExtra("carLocation", selectedCar.location)
            intent.putExtra("uploaderUid", selectedCar.uploaderUid)
            intent.putExtra("descriptionText",selectedCar.descriptionText)
            startActivity(intent)
        }
        binding.recyclerView.adapter = carAdapter

        // Set up Drawer Toggle
        val toggle = ActionBarDrawerToggle(
            this,
            binding.main,
            binding.include.myToolBar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.main.addDrawerListener(toggle)
        toggle.syncState()

        // Set Navigation Item Selected Listener
        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        // get data
        getData()
        // update data

        val ahmet:Int = 22

        println(ahmet+20)


        /*
        // RecyclerView scroll listener

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    // Load next page of data

                    getData()
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        })

         */

    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    /*
    private var lastVisible: DocumentSnapshot? = null
    private var isLoading = false
    private var isLastPage = false
    private val pageSize = 5 // Sayfa başına yüklenen öğe sayısı

     */

    private fun getData() {
        db.collection("Cars")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (value != null) {
                        if (!value.isEmpty) {
                            carArrayList.clear() // Listeyi temizle
                            val documents = value.documents
                            for (document in documents) {
                                val comment = document.getString("comment") ?: "No comment"
                                val downloadUrl = document.getString("downloadUrl") ?: ""
                                val kilometer = document.getString("carKilometer") ?: "Unknown"
                                val carBrand = document.getString("carBrand") ?: "Unknown"
                                val carModel = document.getString("carModel") ?: "Unknown"

                                // Retrieve the date as Timestamp and format it
                                val timestamp = document.getTimestamp("date")
                                val adsDate = if (timestamp != null) {
                                    val date = timestamp.toDate()
                                    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                    sdf.format(date)  // Format the date to "dd.MM.yyyy"
                                } else {
                                    "Unknown"
                                }

                                val carYear = document.getString("carYear") ?: "Unknown"
                                val carLocation = document.getString("carLocation") ?: "Unknown"
                                val carPrice = document.getString("carPrice") ?: "Unknown"
                                val userEmail = document.getString("userEmail") ?: "Unknown"
                                val uploaderUid = document.getString("uploaderUid") ?: "Unknown" // Retrieve uploaderUid
                                val descriptionText = document.getString("descriptionText") ?: "Unknown"
                                val car = Car(
                                    comment,
                                    carBrand,
                                    carModel,
                                    carYear,
                                    adsDate,
                                    carLocation,
                                    carPrice,
                                    kilometer,
                                    downloadUrl,
                                    userEmail,
                                    uploaderUid,
                                    descriptionText
                                )
                                carArrayList.add(car)
                            }
                            binding.recyclerView.adapter?.notifyDataSetChanged() // RecyclerView'i güncelle
                        }
                    }
                }
            }
    }


    fun userButton(view: View) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Kullanıcı zaten giriş yapmış
            Toast.makeText(this@MainActivity, "Zaten giriş yaptınız!", Toast.LENGTH_SHORT).show()
        } else {
            // Kullanıcı giriş yapmamış, UserActivity'e yönlendir
            val intent = Intent(this@MainActivity, UserActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                Toast.makeText(this, "Ana Sayfa", Toast.LENGTH_LONG).show()
                // Show RecyclerView and hide fragment
                showRecyclerView()
            }

            R.id.nav_add_item -> {
                openFragment(AddPostFragment())
                // Hide RecyclerView and show fragment
                hideRecyclerView()
            }

            R.id.nav_car_item -> {
                openFragment(BrandFragment())
                // Hide RecyclerView and show fragment
                hideRecyclerView()
            }

            R.id.nav_sign_out -> {
                auth.signOut()
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.main.closeDrawer(GravityCompat.START)
        return true


    }

    fun showRecyclerView() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }

    fun hideRecyclerView() {
        binding.recyclerView.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
    }


    override fun onBackPressed() {
        if (binding.main.isDrawerOpen(GravityCompat.START)) {
            binding.main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        // Ensure fragmentManager is initialized
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }


}
