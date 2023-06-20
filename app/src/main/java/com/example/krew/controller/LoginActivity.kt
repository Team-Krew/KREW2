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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.messaging.FirebaseMessaging
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
                Log.d(TAG, "resultCode : ${result.resultCode}")
                Log.d(TAG, "result : $result")
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
                                                if(user_email == null){ //처음 로그인 한 경우
                                                    FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                                        OnCompleteListener { task ->
                                                            if (!task.isSuccessful) {
                                                                Log.d("Firebase Communication", "Fetching FCM registration token failed ${task.exception}")
                                                                return@OnCompleteListener
                                                            }
                                                            val deviceToken = task.result
                                                            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                                                            cur_user2 = User(firebaseAuth.currentUser!!.uid, deviceToken, email, account.displayName.toString(), "", "", "", )
                                                            ApplicationClass.cur_user = cur_user2
                                                            database.child("User").child(firebaseAuth.currentUser!!.uid).setValue(cur_user2)
                                                            ApplicationClass.spEditor.putString("user_email", email).apply()
                                                            ApplicationClass.spEditor.putString("user_uid", cur_user2.user_id).apply()
                                                            intent.putExtra("user_token", deviceToken)
                                                            startActivity(intent)
                                                        })
                                                }else{  //기 로그인인 경우
                                                    cur_user = firebaseAuth.currentUser!!
                                                    database.child("User").child(cur_user.uid).get().addOnSuccessListener {
                                                        val intent = Intent(this, MainActivity::class.java)
                                                        cur_user2 = it.getValue<User>() as User
                                                        ApplicationClass.cur_user = cur_user2
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