package com.example.moviemate

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.widget.Toast
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    private lateinit var tvProfileNickname: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var matETProfileNickname: TextInputEditText
    private lateinit var matETCurrentPassword: TextInputEditText
    private lateinit var matETNewPassword: TextInputEditText
    private lateinit var btnUpdateProfile: MaterialButton
    private var editProfileEnabled = false
    private var nickname = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = (activity as MainActivity).auth
        tvProfileNickname = view.findViewById(R.id.tvProfileNickname)
        matETProfileNickname = view.findViewById(R.id.matETProfileNickname)
        btnUpdateProfile = view.findViewById<MaterialButton>(R.id.btnUpdateProfile)

        // Handle Sign Out
        signOutBtn = view.findViewById<Button>(R.id.btnSignOut)
        signOutBtn.setOnClickListener { handleSignOut() }

        tvProfileEmail = view.findViewById(R.id.tvProfileEmail)
        tvProfileEmail.text = auth.currentUser?.email

        if (auth.currentUser!!.displayName.isNullOrEmpty()) {
            nickname = "Adventurer"
        } else {
            nickname = auth.currentUser!!.displayName!!
        }



        // Enable update profile button when the user edits text in the profile section
        matETCurrentPassword = view.findViewById(R.id.matETProfileCurrentPassword)
        matETCurrentPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!matETCurrentPassword.text.isNullOrEmpty()) {
                    btnUpdateProfile.isEnabled = true
                } else {
                    btnUpdateProfile.isEnabled = false
                }
            }
        })
        matETNewPassword = view.findViewById(R.id.matETProfileNewPassword)
        matETNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!matETNewPassword.text.isNullOrEmpty())
                    btnUpdateProfile.isEnabled = true
                else
                    btnUpdateProfile.isEnabled = false
            }
        })
        matETProfileNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != nickname) {
                    btnUpdateProfile.isEnabled = true
                } else {
                    btnUpdateProfile.isEnabled = false
                }
            }
        })
        return view
    }

    private fun handleProfileUpdate() {}

    override fun onResume() {
        super.onResume()
        // Update nickname and email
        tvProfileNickname.text = this.nickname
        matETProfileNickname.setText(this.nickname)
    }

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

}