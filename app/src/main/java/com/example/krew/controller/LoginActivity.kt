package com.example.krew.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.databinding.ActivityLoginBinding
import com.example.krew.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val TAG = this.javaClass.simpleName

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var cur_user: FirebaseUser
    private lateinit var cur_user2 : User
    val database = FirebaseDatabase.getInstance().getReference()
    private var email: String = ""
    private var tokenId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), ActivityResultCallback { result ->
                Log.e(TAG, "resultCode : ${result.resultCode}")
                Log.e(TAG, "result : $result")
                if (result.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        task.getResult(ApiException::class.java)?.let { account ->
                            tokenId = account.idToken
                            if (tokenId != null && tokenId != "") {
                                val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                                firebaseAuth.signInWithCredential(credential)
                                    .addOnCompleteListener {
                                        if (firebaseAuth.currentUser != null) {
                                            email = account.email.toString()
                                            val googleSignInToken = account.idToken ?: ""
                                            if (googleSignInToken != "") {
                                                Log.d(TAG, "googleSignInToken : $googleSignInToken")
                                                val user_email = ApplicationClass.sSharedPreferences.getString("user_email", null)
                                                if(user_email == null){
                                                    val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                                                    ApplicationClass.spEditor.putString("user_email", email).apply()
                                                    startActivity(intent)
                                                }else{
                                                    cur_user = firebaseAuth.currentUser!!
                                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                    database.child("User").child(cur_user.uid).get().addOnSuccessListener {
                                                        val intent = Intent(this, MainActivity::class.java)
                                                        cur_user2 = it.getValue<User>() as User
                                                        intent.putExtra("cur_user", cur_user2)
                                                        startActivity(intent)
                                                    }

                                                }
                                            } else {
                                                Log.e(TAG, "googleSignInToken이 null")
                                            }
                                        }
                                    }
                            }
                        } ?: throw Exception()
                    }   catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })

        binding.run {
            loginBtn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

                    val signInIntent: Intent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                    gso.account
                }
            }
        }
    }
}