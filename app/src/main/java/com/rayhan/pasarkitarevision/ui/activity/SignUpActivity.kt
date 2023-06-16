package com.rayhan.pasarkitarevision.ui.activity.SignUpActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rayhan.pasarkitarevision.data.remote.ApiService
import com.rayhan.pasarkitarevision.databinding.ActivitySignUpBinding
import com.rayhan.pasarkitarevision.model.UserRegistrationRequest
import com.rayhan.pasarkitarevision.ui.activity.SignInActivity.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("users")

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://34.136.73.74:3000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.signInTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val username = binding.usernameEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()
                && username.isNotEmpty()
            ) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                val userId = user?.uid

                                // Create UserRegistrationRequest object
                                val request = UserRegistrationRequest(username, email, pass)

                                // Save the user data to Firebase Realtime Database
                                val userData = com.rayhan.pasarkitarevision.model.UserData(
                                    username,
                                    email
                                )
                                if (userId != null) {
                                    reference.child(userId).setValue(userData)
                                        .addOnSuccessListener {
                                            // Registration and data saving successful

                                            // Send the registration request to the server
                                            CoroutineScope(Dispatchers.IO).launch {
                                                try {
                                                    val response = apiService.registerUser(request)
                                                    if (response.isSuccessful) {
                                                        val responseBody = response.body()
                                                        if (responseBody?.message == "User registered successfully!") {
                                                            // Registration successful
                                                            runOnUiThread {
                                                                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                                                                startActivity(intent)
                                                                finish()
                                                            }
                                                            showToast("Registration successful")
                                                            Log.d("SignUpActivity", "Registration successful")
                                                        } else {
                                                            val errorMessage = response.errorBody()?.string()
                                                            showToast("Registration failed: $errorMessage")
                                                            Log.e("SignUpActivity", "Registration failed: $errorMessage")
                                                        }
                                                    } else {
                                                        val errorMessage =
                                                            response.errorBody()?.string()
                                                        showToast("Registration failed: $errorMessage")
                                                        Log.e(
                                                            "SignUpActivity",
                                                            "Registration failed: $errorMessage"
                                                        )
                                                    }
                                                } catch (e: Exception) {
                                                    showToast("Registration failed: ${e.message}")
                                                    Log.e(
                                                        "SignUpActivity",
                                                        "Registration failed: ${e.message}"
                                                    )
                                                }
                                            }
                                        }
                                        .addOnFailureListener { error ->
                                            showToast("Failed to register user")
                                            Log.e("SignUpActivity", "Failed to register user")
                                        }
                                }
                            } else {
                                val errorMessage = authTask.exception?.message
                                showToast("Registration failed: ${authTask.exception.toString()}")
                                Log.e(
                                    "SignUpActivity",
                                    "Firebase authentication failure: $errorMessage"
                                )
                            }
                        }
                } else {
                    showToast("Password does not match")
                    Log.e("SignUpActivity", "Password does not match")
                }
            } else {
                showToast("Empty fields are not allowed")
                Log.e("SignUpActivity", "Empty fields are not allowed")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
