package com.example.krew.controller

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.adapter.GroupFBAdapter
import com.example.krew.adapter.MemberRVAdapter
import com.example.krew.databinding.ActivityGroupBinding
import com.example.krew.model.Calendar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.MutableMap
import kotlin.collections.arrayListOf
import kotlin.collections.set

/*
* 입력에 특수문자 제한해야함
* 키보드 올라오면 리사이클러뷰 사라지는거 해결
*   - 이전 문제 : 키보드의 '완료' 버튼을 누르지 않고 바로 다이얼로그의 버튼을 누르면
                ime 에러와 함께 리사이클러뷰에 포함되지 않음

* 초대장 보내기 -> 유저에 토큰 저장
*             -> 토큰을 통해 메세지 전달 postman 참고
*             -> 날라온 정보를 저장하여 바로 응답하지 않더라도 남겨놓았다가 해결
*             -> 새로운 창을 만들어서 날라온 요청들에 응답
* */
class GroupActivity : AppCompatActivity() {

    lateinit var binding: ActivityGroupBinding
    lateinit var memberRVAdapter: MemberRVAdapter
    val userArr = ArrayList<String>()

    var curCalendarID: String? = null
    var colorCode: Int = 0
    val colorArr = arrayListOf(
        R.color.color_gr0,
        R.color.color_gr1,
        R.color.color_gr2,
        R.color.color_gr3,
        R.color.color_gr4,
        R.color.color_gr5,
        R.color.color_gr6,
        R.color.color_gr7,
        R.color.color_gr8,
        R.color.color_gr9
    )
    val rbArr = arrayListOf(
        R.id.color_gr0, R.id.color_gr1, R.id.color_gr2, R.id.color_gr3, R.id.color_gr4,
        R.id.color_gr5, R.id.color_gr6, R.id.color_gr7, R.id.color_gr8, R.id.color_gr9,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        curCalendarID = intent.getStringExtra("id")
        initRV()
        init()
        if (curCalendarID != null) {
            connectFB();
        }
    }

    private fun initRV() {
        memberRVAdapter = MemberRVAdapter(userArr)
        binding.rvGroup.adapter = memberRVAdapter
        binding.rvGroup.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        memberRVAdapter.itemClickListener = object : MemberRVAdapter.OnItemClickListener {
            override fun OnItemClick(position: Int) {
                userArr.removeAt(position)
                memberRVAdapter.notifyItemRemoved(position)
            }
        }
    }

    private fun init() {
        binding.apply {
            /*확인 버튼 클릭시 이벤트 처리*/
            var str: String? = null
            var flag = false
            ivCheck.setOnClickListener {
                if (etGroupName.text.isNullOrBlank()) {
                    str = "그룹이름을 적어주세요"
                } else if (etGroupInfo.text.isNullOrBlank()) {
                    str = "설명을 적어주세요"
                } else if (colorCode == 0) {
                    str = "라벨 색상을 선택하세요"
                } else {
                    flag = true
                }

                if (flag) {
                    confirmGroup()
                } else {
                    Toast.makeText(this@GroupActivity, str, Toast.LENGTH_SHORT).show()
                }
            }

            /*RadioButton*/
            colorGr0.setOnClickListener {
                binding.rg2.clearCheck()
                colorCode = colorArr[0]
            }
            colorGr1.setOnClickListener {
                binding.rg2.clearCheck()
                colorCode = colorArr[1]
            }
            colorGr2.setOnClickListener {
                binding.rg2.clearCheck()
                colorCode = colorArr[2]
            }
            colorGr3.setOnClickListener {
                binding.rg2.clearCheck()
                colorCode = colorArr[3]
            }
            colorGr4.setOnClickListener {
                binding.rg2.clearCheck()
                colorCode = colorArr[4]
            }
            colorGr5.setOnClickListener {
                binding.rg1.clearCheck()
                colorCode = colorArr[5]
            }
            colorGr6.setOnClickListener {
                binding.rg1.clearCheck()
                colorCode = colorArr[6]
            }
            colorGr7.setOnClickListener {
                binding.rg1.clearCheck()
                colorCode = colorArr[7]
            }
            colorGr8.setOnClickListener {
                binding.rg1.clearCheck()
                colorCode = colorArr[8]
            }
            colorGr9.setOnClickListener {
                binding.rg1.clearCheck()
                colorCode = colorArr[9]
                //rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }

            val dlg = GroupDialog(this@GroupActivity)
            dlg.setOnClickedListener(object : GroupDialog.ButtonClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClicked(email: String) {
                    Log.e("IMECallback", email)
                    userArr.add(email)
                    Log.d("IMECallback", userArr.toString())
                    binding.rvGroup.adapter?.notifyDataSetChanged()
                }
            })
            btnAddUser.setOnClickListener {
                dlg.show()
            }
        }
    }

    private fun confirmGroup() {
        binding.apply {

            val calendar = Firebase.database.getReference("Calendar")
            if (curCalendarID == null) {
                val db = Firebase.database.reference
                var cNum = 0

                db.child("CNum").get().addOnSuccessListener {
                    Log.e("Firebase communication", "getting cnum val :${it.value.toString()}")
                    cNum = it.value.toString().toInt()

                    val email = ApplicationClass.sSharedPreferences.getString("user_id", null)
                    val cal = Calendar(
                        cNum.toString(),
                        etGroupName.text.toString(),
                        etGroupInfo.text.toString(),
                        colorCode,
                        email,
                        userArr
                    )
                    calendar.child(cNum.toString()).setValue(cal)
                    addtoSharedPreference(cNum.toString())

                    Log.e("Firebase communication", "checking sSharedPreference :" +
                            "${ApplicationClass.sSharedPreferences.getString("calendars", "")}")
                    db.child("CNum").setValue(cNum + 1)
                    db.child("CNum").get().addOnSuccessListener {
                        Log.e(
                            "Firebase communication",
                            "getting cnum val again and again:${it.value.toString()}"
                        )
                    }

                    Toast.makeText(this@GroupActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                val taskMap: MutableMap<String, Any> = HashMap()
                taskMap[curCalendarID!! + "/name"] = etGroupName.text.toString()
                taskMap[curCalendarID!! + "/comment"] = etGroupInfo.text.toString()
                taskMap[curCalendarID!! + "/label"] = colorCode
                taskMap[curCalendarID!! + "/participant"] = userArr
                calendar.updateChildren(taskMap)
                Toast.makeText(this@GroupActivity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun connectFB() {
        val mDatabase = Firebase.database.getReference("Calendar")
        mDatabase.child(curCalendarID!!).get().addOnSuccessListener {
            Log.e("Firebase communication", "value: ${it.value.toString()}")
            val json = JSONObject(it.value.toString())
            binding.apply {
                etGroupName.setText(json.getString("name").toString())
                etGroupInfo.setText(json.getString("comment").toString())
                colorCode = json.getString("label").toInt()

                var i = 0;
                for (code in colorArr) {
                    if (code == colorCode) i = colorArr.indexOf(code);
                }

                if (i < 5) rg1.check(rbArr[i])
                else rg2.check(rbArr[i]);
            }
            if(it.value.toString().contains("participant")){
                val arr = json.getString("participant").removeSurrounding("[", "]").split(",")
                userArr.apply {
                    for (u in arr)
                        add(u.removeSurrounding("\"", "\""))
                }
                Log.e("Firebase communication", userArr.toString())
                binding.rvGroup.adapter?.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Log.e("Firebase communication", "Communication Failure")
        }
    }

    private fun addtoSharedPreference(id: String) {
        var str = ApplicationClass.sSharedPreferences.getString("calendars", null)
        if (str != null) {
            str += ",$id"
        } else {
            str = id
        }
        ApplicationClass.spEditor.putString("calendars", str).apply()
    }

    private fun sendInvitation() {

    }
}