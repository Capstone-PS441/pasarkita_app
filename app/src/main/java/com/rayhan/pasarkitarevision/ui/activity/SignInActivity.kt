package com.rayhan.pasarkitarevision.ui.activity.SignInActivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rayhan.pasarkitarevision.MainActivity
import com.rayhan.pasarkitarevision.data.remote.ApiService
import com.rayhan.pasarkitarevision.databinding.ActivitySignInBinding
import com.rayhan.pasarkitarevision.model.UserLoginRequest
import com.rayhan.pasarkitarevision.ui.activity.SignUpActivity.SignUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

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

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val userId = user?.uid

                            if (userId != null) {
                                signInUserOnServer(email, password)
                            } else {
                                showToast("User ID is null")
                            }
                        } else {
                            showToast("Sign-in failed: ${signInTask.exception.toString()}")
                        }
                    }
            } else {
                showToast("Empty fields are not allowed")
            }
        }
    }

    private fun signInUserOnServer(email: String, password: String) {
        // Create UserLoginRequest object
        val request = UserLoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.loginUser(request)
                if (response.isSuccessful) {
                    val signInResponse = response.body()
                    if (signInResponse != null) {
                        val jwtToken = signInResponse.accessToken

                        if (jwtToken.isNotEmpty()) {
                            // Store the JWT token securely
                            saveJwtToken(jwtToken)
                            Log.d("SignInActivity", "Received JWT token: $jwtToken")
                            navigateToMainActivity()
                        } else {
                            showToast("Failed to get JWT token from server")
                        }
                    } else {
                        showToast("Failed to parse response")
                    }
                } else {
                    showToast("Sign-in failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                showToast("Sign-in failed: ${e.message}")
            }
        }
    }

    private fun saveJwtToken(jwtToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", jwtToken)
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser !=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
