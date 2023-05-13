package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.seoultech.mobileprogramming.high_five.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var btnGoogleSignIn: SignInButton
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleApiClient

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    companion object {
        private const val REQ_SIGN_GOOGLE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Google sign in button 이용 시,  옵션 설정
        val googleSignInOptions: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        // fragment에서는 getContext
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()

        auth = FirebaseAuth.getInstance() // Initialize firebase Authenticate Object

        binding.btnGoogle.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient) // google이 만들어놓은 intent로 넘어옴
            startActivityForResult(intent, Companion.REQ_SIGN_GOOGLE);
        }

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
//        TODO("Not yet implemented")
    }

    // 결과값 되돌려 받는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == REQ_SIGN_GOOGLE) {
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

