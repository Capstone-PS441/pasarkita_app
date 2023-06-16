package com.rayhan.pasarkitarevision.ui.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.rayhan.pasarkitarevision.R
import com.rayhan.pasarkitarevision.databinding.FragmentProfilBinding
import com.rayhan.pasarkitarevision.ui.activity.SignInActivity.SignInActivity

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var usernameTextView: TextView
    private lateinit var currentUserTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        auth = FirebaseAuth.getInstance()
        currentUserTextView = view.findViewById(R.id.currentUserTextView)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        logoutButton = view.findViewById(R.id.logoutButton)

        // Set the current user's email to the TextView
        val currentUser = auth.currentUser
        val currentUserEmail = currentUser?.email
        currentUserTextView.text = currentUserEmail
        database = FirebaseDatabase.getInstance().reference

        val currentUserId = currentUser?.uid
        currentUserId?.let {
            val userRef = database.child("users").child(currentUserId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val username = dataSnapshot.child("username").value as? String
                    usernameTextView.text = username
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Log Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
