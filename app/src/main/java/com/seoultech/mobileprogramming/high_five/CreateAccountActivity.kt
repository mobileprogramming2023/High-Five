package com.seoultech.mobileprogramming.high_five

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoultech.mobileprogramming.high_five.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {

        var auth: FirebaseAuth = Firebase.auth

        Log.d("test", "createAccount()")
        Log.d("test", auth.toString())

        // Initialize Firebase Auth
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    Toast.makeText(
//                        baseContext,
//                        "Create Account success.",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                    val user = auth.currentUser
//                    onBackPressed();
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext,
//                        task.exception?.message,
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                }
//            }
    }

}