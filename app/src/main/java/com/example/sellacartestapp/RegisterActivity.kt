package com.example.sellacartestapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sellacartestapp.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.registerSignUpButton.setOnClickListener { signUp() }

        binding.registerAlreadyHaveAccount.setOnClickListener {
            val intent = Intent(this,UserActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    private fun signUp() {
        val name = binding.registerNameText.text.toString()
        val surname = binding.registerSurnameText.text.toString()
        val phone = binding.registerPhoneText.text.toString()
        val email = binding.registerEmailText.text.toString()
        val password = binding.registerPasswordText.text.toString()

        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_LONG).show()
        } else {
            // Firebase Authentication ile kullanıcıyı oluştur
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // Kullanıcı başarıyla oluşturuldu, Firestore'a ek bilgileri kaydet
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        saveUserToFirestore(uid, name, surname, phone, email)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Hoş Geldiniz!", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun saveUserToFirestore(uid: String, name: String, surname: String, phone: String, email: String) {
        val user = hashMapOf(
            "name" to name,
            "surname" to surname,
            "phone" to phone,
            "email" to email
        )

        firestore.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Kullanıcı bilgileri başarıyla kaydedildi!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Kullanıcı bilgileri kaydedilirken hata oluştu: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }


}