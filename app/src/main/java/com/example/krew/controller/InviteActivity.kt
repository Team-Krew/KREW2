package com.example.krew.controller

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.databinding.ActivityInviteBinding
import com.example.krew.model.Invitation
import com.google.firebase.database.FirebaseDatabase

class InviteActivity : AppCompatActivity() {
    val TAG = "Firebase Communication"
    lateinit var binding:ActivityInviteBinding
    lateinit var invitation: Invitation
    var key:String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            invitation = intent.getSerializableExtra("invite", Invitation::class.java) as Invitation
        } else {
            invitation = intent.getSerializableExtra("invite") as Invitation
        }
        key = intent.getStringExtra("key")
        init()
    }

    private fun init() {
        binding.apply {
            tvCalendarName.text = invitation.calendar_name
            tvAdminName.text = "From. ${invitation.admin}"
            tvTargetName.text = "To. ${invitation.target}"

            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Invitation/$key")
            btnAccept.setOnClickListener {
                Log.e(TAG, "calendar_key: ${key}, calendar_id: ${invitation.calendar_id}")

                var str = ApplicationClass.sSharedPreferences.getString("calendars", null)
                if (str != null) {
                    str += ",${invitation.calendar_id}"
                } else {
                    str = invitation.calendar_id
                }
                ApplicationClass.spEditor.putString("calendars", str).apply()

                ref.removeValue()
                    .addOnSuccessListener {
                        Log.e(TAG, "Data Deleted")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Data Deleted -> $exception")
                    }
                Toast.makeText(this@InviteActivity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

            btnReject.setOnClickListener {
                Toast.makeText(this@InviteActivity, "거절하였습니다.", Toast.LENGTH_SHORT).show()
                ref.removeValue()
                    .addOnSuccessListener {
                        Log.e(TAG, "Data Deleted")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Data Deleted -> $exception")
                    }
                finish()
            }
        }
    }


}