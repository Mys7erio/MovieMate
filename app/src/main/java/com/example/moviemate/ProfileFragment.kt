package com.example.moviemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.widget.Toast
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button

    private fun handleSignOut() {
        // FLAG_ACTIVITY_CLEAR_TASK:
        // When set, this flag instructs Android to clear the entire task, including any existing activities,
        // associated with the activity being launched. It's commonly used when starting a new "root" activity to reset
        // the task stack and remove all existing activities.

        // FLAG_ACTIVITY_NEW_TASK:
        // When set, this flag tells Android to create a new task if the activity is being started from outside of an existing task,
        // such as from a different application or from a service. It's typically used to ensure that the new activity is placed
        // in a new task, becoming the root of that task.
        Toast.makeText(requireContext(), "Logging Out...", Toast.LENGTH_SHORT).show()
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        signOutBtn = view.findViewById<Button>(R.id.btnSignOut)

        // Handle Sign Out
        signOutBtn.setOnClickListener { handleSignOut() }
        return view
    }

}