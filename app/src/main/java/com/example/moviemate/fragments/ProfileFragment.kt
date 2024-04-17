package com.example.moviemate.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.moviemate.LoginActivity
import com.example.moviemate.MainActivity
import com.example.moviemate.R
import com.example.moviemate.api.setImage
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signOutBtn: Button
    private lateinit var tvProfileNickname: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var matETProfileNickname: TextInputEditText
    private lateinit var matETCurrentPassword: TextInputEditText
    private lateinit var matETNewPassword: TextInputEditText
    private lateinit var btnUpdateProfile: MaterialButton
    private lateinit var ivProfileImage: ImageView

    private lateinit var requestQueue: RequestQueue
    private var nickname = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = (activity as MainActivity).auth
        requestQueue = Volley.newRequestQueue(requireContext())
        tvProfileNickname = view.findViewById(R.id.tvProfileNickname)
        matETProfileNickname = view.findViewById(R.id.matETProfileNickname)
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile)
        ivProfileImage = view.findViewById(R.id.ivProfileProfileImage)

        // Handle Sign Out
        signOutBtn = view.findViewById(R.id.btnSignOut)
        signOutBtn.setOnClickListener { handleSignOut() }

        tvProfileEmail = view.findViewById(R.id.tvProfileEmail)
        tvProfileEmail.text = auth.currentUser?.email

        nickname = if (auth.currentUser!!.displayName.isNullOrEmpty()) {
            "Adventurer"
        } else {
            auth.currentUser!!.displayName!!
        }

        // Add handler for profile updation
        customiseUpdateProfileBtn(view)
        btnUpdateProfile.setOnClickListener { handleProfileUpdate() }

        return view
    }


    override fun onResume() {
        super.onResume()
        handleNicknameChange()
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


    private fun handleProfileUpdate() {
        if (matETProfileNickname.text!!.isNotEmpty()) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(matETProfileNickname.text.toString())
                .build()
            auth.currentUser!!.updateProfile(profileUpdates)
            Toast.makeText(requireContext(), "Nickname Changed", Toast.LENGTH_LONG).show()

            // Update nickname and email
            nickname = matETProfileNickname.text.toString()
            handleNicknameChange()
            btnUpdateProfile.isEnabled = false
        }
    }


    private fun handleNicknameChange() {
        // Update nickname and email
        tvProfileNickname.text = nickname
        matETProfileNickname.setText(nickname)

        var profileImageUrl = "https://api.dicebear.com/8.x/adventurer/png"
        profileImageUrl += "?backgroundColor=transparent"
        profileImageUrl += "&seed=$nickname"

        setImage(requestQueue, profileImageUrl) {
            ivProfileImage.setImageBitmap(it)
        }
    }


    private fun customiseUpdateProfileBtn(view: View) {
        // Enable update profile button when the user edits text in the profile section
        matETCurrentPassword = view.findViewById(R.id.matETProfileCurrentPassword)
        matETCurrentPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                btnUpdateProfile.isEnabled = !matETCurrentPassword.text.isNullOrEmpty()
            }
        })
        matETNewPassword = view.findViewById(R.id.matETProfileNewPassword)
        matETNewPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                btnUpdateProfile.isEnabled = !matETNewPassword.text.isNullOrEmpty()
            }
        })
        matETProfileNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                btnUpdateProfile.isEnabled = s.toString() != nickname
            }
        })
    }

}