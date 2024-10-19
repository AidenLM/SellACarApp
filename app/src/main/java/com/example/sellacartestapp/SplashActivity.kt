package com.example.sellacartestapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sellacartestapp.databinding.ActivitySplashBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.util.logging.Handler

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        android.os.Handler().postDelayed({
            val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val isFirstTime = sharedPreferences.getBoolean("is_first_time", true)
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

            if (isFirstTime == true || currentUser == null) {
                // İlk kez giriş yapıyor, "Haydi Başlayalım" ekranını göster
                val editor = sharedPreferences.edit()
                editor.putBoolean("is_first_time", false)
                editor.apply()

                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            } else if (currentUser == null) {
                // Kullanıcı daha önce giriş yaptıysa, normal akışa yönlendir
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }else{

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }




        }, 1500)





    }
}