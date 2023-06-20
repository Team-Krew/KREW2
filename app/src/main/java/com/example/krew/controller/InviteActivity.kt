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
import com.example.krew.model.Calendar
import com.example.krew.model.Invitation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class InviteActivity : AppCompatActivity() {
    val TAG = "Invitation Communication"
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
                Log.e(TAG, "Granted: calendar_key: ${key}, calendar_id: ${invitation.calendar_id}")

                ref.removeValue()
                    .addOnSuccessListener {
                        Log.e(TAG, "Data Deleted")
                        Toast.makeText(this@InviteActivity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Data not Deleted -> $exception")
                    }
            }

            btnReject.setOnClickListener {
                Toast.makeText(this@InviteActivity, "거절하였습니다.", Toast.LENGTH_SHORT).show()

                val mDatabase = Firebase.database.getReference("Calendar/${invitation.calendar_id}")
                mDatabase.get().addOnSuccessListener {
                    Log.e(TAG, "value: ${it.value.toString()}")
                    val cal: Calendar
                    cal = it.getValue<Calendar>() as Calendar
                    val arr = cal.Participant

                    for(email in cal.Participant!!){
                        if(email == invitation.target){
                            arr!!.remove(email)
                            break
                        }
                    }

                    Log.e(TAG, "Denied: id ${cal.calendar_id} reject invitation arr => ${arr.toString()}")
                    val updates = HashMap<String, Any>()
                    updates["participant"] = arr as List<Any>
                    mDatabase.updateChildren(updates)

                    ref.removeValue()
                        .addOnSuccessListener {
                            Log.e(TAG, "Data Deleted: Rejected request")
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Data Deleting Fail: Rejected Request -> $exception")
                        }

                }.addOnFailureListener {
                    Log.e("Firebase communication", "Communication Failure")
                }


            }
        }
    }


}