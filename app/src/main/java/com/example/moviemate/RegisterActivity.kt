package com.example.moviemate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.widget.TextView
import android.widget.EditText
import android.content.Intent
import android.util.Log
import android.widget.Toast
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signInText: TextView
    private lateinit var etNickname: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfPassword: EditText
    private lateinit var btnRegister: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        etNickname = findViewById(R.id.etRegNickname)
        etUsername = findViewById(R.id.etRegUsername)
        etPassword = findViewById(R.id.etRegPassword)
        etConfPassword = findViewById(R.id.etRegConfPassword)
        btnRegister = findViewById(R.id.btnRegRegister)

        signInText = findViewById(R.id.signInLink)
        signInText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            handleRegistration()
        }
    }

    private fun handleRegistration() {
        val nickname = etNickname.text.toString()
        val email = etUsername.text.toString()
        val password = etPassword.text.toString()
        val confPassword = etConfPassword.text.toString()

        // Check if email and passwords are not empty
        if (email == "" || password == "") {
            Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if password and confirm passwords match
        if (!password.equals(confPassword)) {
            Toast.makeText(this, "Passwords do no match", Toast.LENGTH_SHORT).show()
            return
        }

        // Register user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    // If nickname (displayname) is provided, add it after account creation
                    // This is the only way :/
                    if (nickname != "") {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname)
                            .build()
                        user!!.updateProfile(profileUpdates)
                    }

                    // Toast registration successful message and navigate to Home Page
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                    // Sleeping for 1s to make sure that the username gets updated in the home page
                    Thread.sleep(1000)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    val errorInfo = task.exception?.message
                    Log.e("MovieMate", errorInfo.toString())
                    Toast.makeText(this, "Failed to create account: $errorInfo", Toast.LENGTH_SHORT).show()
                }
            }
    }
}