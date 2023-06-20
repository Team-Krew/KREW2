package com.example.krew.controller

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.krew.ApplicationClass
import com.example.krew.ApplicationClass.Companion.cur_user
import com.example.krew.R
import com.example.krew.databinding.ActivityProfileBinding
import com.example.krew.model.User
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    val cur_user = FirebaseAuth.getInstance().currentUser!!
    lateinit var cur_user2: User
    lateinit var user_token: String
    val database = FirebaseDatabase.getInstance().getReference()

    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val location = it.data?.getStringExtra("formattedAddress")
                if(!location.isNullOrBlank())
                    binding.profileEditStartLoc.setText(location)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apikey = getString(R.string.apiKey)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apikey)
        }
        binding.apply {
            profileEditName.setText(profileVar.name.toString())
            profileEditSelfIntro.setText(profileVar.intro.toString())
            profileEditStartLoc.setText(profileVar.address.toString())
            profileEditReadyTime.setText(profileVar.time.toString())
        }
        initLayout()
    }

    private fun initLayout() {
        binding.profileUsername.text = cur_user.displayName.toString()
        binding.profileEditName.setText(cur_user.displayName.toString())
        user_token = intent.getStringExtra("user_token")!!
        database.child("User").child(cur_user.uid).get().addOnSuccessListener {
            cur_user2 = it.getValue<User>()!!
            binding.apply {
                profileUsername.text = cur_user2.name
                profileEditName.setText(cur_user2.name)
                profileEditSelfIntro.setText(cur_user2.comment)
                profileEditReadyTime.setText(cur_user2.time)
                profileEditStartLoc.setText(cur_user2.address)
            }
        }
        binding.profileGoBackButton.setOnClickListener{
            finish()
        }

        binding.profileCheckButton.setOnClickListener{
            val edt_name = binding.profileEditName.text.toString()
            val ready_time = binding.profileEditReadyTime.text.toString()
            val comment = binding.profileEditSelfIntro.text.toString()
            val address = binding.profileEditStartLoc.text.toString()
            val uid = cur_user.uid
            val user_email = ApplicationClass.sSharedPreferences.getString("user_email", null)
            val tempUser = User(uid, user_token, user_email!!, edt_name, address, comment, ready_time,)
            database.child("User").child(tempUser.user_id).setValue(tempUser)
            Toast.makeText(this, "회원 정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("cur_user", cur_user2)
            startActivity(intent)
        }

        binding.profileEditStartLoc.setOnClickListener {
            backupDataBeforeIntent()
            val intent =
                Intent(this@ProfileActivity, ProgrammaticAutocompleteGeocodingActivity::class.java)
            //intent.putExtra("selected_date", today)
            activityResultLauncher.launch(intent)
        }
        if (intent.hasExtra("formattedAddress")) {
            val formattedAddr = intent.getStringExtra("formattedAddress")
            binding.profileEditStartLoc.setText(formattedAddr.toString())
        }

    }

    fun backupDataBeforeIntent(){
        binding.apply {
            profileVar.name = binding.profileEditName.textAlignment.toString()
            profileVar.intro = binding.profileEditSelfIntro.textAlignment.toString()
            profileVar.time = binding.profileEditReadyTime.textAlignment.toString()
        }
    }

    fun clearVar() {
        profileVar.name = ""
        profileVar.intro = ""
        profileVar.time = ""
    }
}