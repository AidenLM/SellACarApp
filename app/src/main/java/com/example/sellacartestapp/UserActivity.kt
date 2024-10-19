package com.example.sellacartestapp

import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sellacartestapp.databinding.ActivityMainBinding
import com.example.sellacartestapp.databinding.ActivityUserBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth


class UserActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        binding.loginPageBackButton.setOnClickListener {

            if (currentUser == null){
                val intent = Intent(this@UserActivity,RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@UserActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }


    public fun singInClicked(view: View){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Mailinizi ve Şifrenizi Giriniz!",Toast.LENGTH_LONG).show()
        }else {

            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                // Success
                val intent = Intent(this@UserActivity,MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@UserActivity,"Hoş Geldiniz!",Toast.LENGTH_LONG).show()
                finish()

            }.addOnFailureListener {
                //Failure
                Toast.makeText(this@UserActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }
    /*
    public fun signUpClicked(view: View){

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Mailinizi ve Şifrenizi Giriniz!",Toast.LENGTH_LONG).show()
        }else {

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                // Success
                val intent = Intent(this@UserActivity,MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@UserActivity,"Hoş Geldiniz!",Toast.LENGTH_LONG).show()
                finish()

            }.addOnFailureListener {
                //Failure
                Toast.makeText(this@UserActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }


    }
       */


}