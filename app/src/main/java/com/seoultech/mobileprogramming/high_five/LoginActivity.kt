package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoultech.mobileprogramming.high_five.DTO.User
import com.seoultech.mobileprogramming.high_five.databinding.ActivityLoginBinding
import java.io.FileInputStream


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var btnGoogleSignIn: SignInButton
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleApiClient

    private val database = Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)
    val databaseReference = database.getReference("user")

    companion object {
        private const val REQ_SIGN_GOOGLE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() // Initialize firebase Authenticate Object

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", currentUser.displayName)
            intent.putExtra("photoUrl", currentUser.photoUrl.toString())
            startActivity(intent)
        }

        val googleSignInOptions: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()

        binding.btnGoogle.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, REQ_SIGN_GOOGLE);
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("highfive", "connection failed")
//        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_SIGN_GOOGLE) {
            var result: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result != null) {
                if (result.isSuccess()) {
                    val account: GoogleSignInAccount? = result.signInAccount
                    resultLogin(account)
                }
            }
        }
    }

    private fun resultLogin(account: GoogleSignInAccount?) {
        if (account != null) {
            val authCredential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, OnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Login Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val user = User(
                            userId = auth.currentUser!!.uid.toString(),
                            userName = auth.currentUser!!.displayName.toString(),
                            userPhotoUri = auth.currentUser!!.photoUrl.toString()
                        )
                        databaseReference.child(auth.currentUser!!.uid.toString()).setValue(user)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("name", account.displayName)
                        intent.putExtra("photoUrl", account.photoUrl.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Login Fail",
                            Toast.LENGTH_SHORT,
                        ).show()
                        // TODO 로그인 실패 시 처리 구현
                    }
                })
        }
    }


}

