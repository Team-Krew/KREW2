package com.example.krew.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.krew.R
import com.example.krew.databinding.ActivityMainBinding
import com.example.krew.databinding.ActivityProfileBinding
import com.example.krew.model.User
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
    val database = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.profileName.text = cur_user.displayName.toString()
        binding.profileEditName.setText(cur_user.displayName.toString())

        database.child("User").child(cur_user.uid).get().addOnSuccessListener {
            cur_user2 = it.getValue<User>()!!
            binding.apply {
                profileName.text = cur_user2.name
                profileEditName.setText(cur_user2.name)
                profileEditSelfIntro.setText(cur_user2.comment)
                profileEditReadyTime.setText(cur_user2.time)
                profileEditStartLoc.setText(cur_user2.address)
            }
        }
        binding.profileGoBackButton.setOnClickListener{
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
        }

        binding.profileCheckButton.setOnClickListener{
            val edt_name = binding.profileEditName.text.toString()
            val ready_time = binding.profileEditReadyTime.text.toString()
            val comment = binding.profileEditSelfIntro.text.toString()
            val address = binding.profileEditStartLoc.text.toString()
            val uid = cur_user.uid
            val tempUser = User(uid, edt_name, address, comment, ready_time)
            database.child("User").child(tempUser.user_id).setValue(tempUser)
            Toast.makeText(this, "회원 정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("cur_user", cur_user2)
            startActivity(intent)
        }
    }
}