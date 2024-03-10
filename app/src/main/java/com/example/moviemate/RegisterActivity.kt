package com.example.moviemate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.widget.TextView
import android.widget.EditText
import android.content.Intent
import android.widget.Toast
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signInText: TextView
    private lateinit var etNickname: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        etNickname = findViewById(R.id.etRegNickname)
        etUsername = findViewById(R.id.etRegUsername)
        etPassword = findViewById(R.id.etRegPassword)
        btnRegister = findViewById(R.id.btnRegRegister)

        signInText = findViewById(R.id.signInLink)
        signInText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            handleRegistration(
                etNickname.text.toString(),
                etUsername.text.toString(),
                etPassword.text.toString()
            )
        }
    }

    private fun handleRegistration(nickname: String, email: String, password: String) {
        if (email == "" || password == "") {
            Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                }
            }
    }
}