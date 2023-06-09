package com.example.krew.controller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krew.ApplicationClass
import com.example.krew.R
import com.example.krew.adapter.MemberFBAdapter
import com.example.krew.adapter.MemberRVAdapter
import com.example.krew.databinding.ActivityGroupBinding
import com.example.krew.model.Calendar
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/*
1.  키보드의 '완료' 버튼을 누르지 않고 바로 다이얼로그의 버튼을 누르면
    ime 에러와 함께 리사이클러뷰에 포함되지 않음
 */
class GroupActivity : AppCompatActivity() {
    lateinit var binding:ActivityGroupBinding
    lateinit var memberRVAdapter: MemberRVAdapter
    lateinit var memberFBAdapter: MemberFBAdapter
    var type:String ?= null
    var colorCode:Int = 0
    val colorArr =  arrayListOf(
        R.color.color_gr0,R.color.color_gr1,R.color.color_gr2,R.color.color_gr3,R.color.color_gr4,
        R.color.color_gr5,R.color.color_gr6,R.color.color_gr7,R.color.color_gr8,R.color.color_gr9
    )

    val userArr = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type = intent.getStringExtra("type")
        initRV()
        init()
    }

    private fun init(){
        binding.apply {
            /*확인 버튼 클릭시 이벤트 처리*/
            var str:String ?= null
            var flag = false
            ivCheck.setOnClickListener {
                if(etGroupName.text.isNullOrBlank()){
                    str = "그룹이름을 적어주세요"
                } else if(etGroupInfo.text.isNullOrBlank()){
                    str = "설명을 적어주세요"
                } else if(colorCode == 0){
                    str = "라벨 색상을 선택하세요"
                } else {
                    flag = true
                }

                if(flag){
                    confirmGroup()
                    Toast.makeText(this@GroupActivity, "등록되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                } else{
                    Toast.makeText(this@GroupActivity, str, Toast.LENGTH_SHORT).show()
                }
            }

            /*RadioButton*/
            colorGr0.setOnClickListener {
                binding.rg2.clearCheck()
                colorCode = colorArr[0]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr1.setOnClickListener{
                binding.rg2.clearCheck()
                colorCode = colorArr[1]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr2.setOnClickListener{
                binding.rg2.clearCheck()
                colorCode = colorArr[2]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr3.setOnClickListener{
                binding.rg2.clearCheck()
                colorCode = colorArr[3]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr4.setOnClickListener{
                binding.rg2.clearCheck()
                colorCode = colorArr[4]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr5.setOnClickListener{
                binding.rg1.clearCheck()
                colorCode = colorArr[5]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr6.setOnClickListener{
                binding.rg1.clearCheck()
                colorCode = colorArr[6]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr7.setOnClickListener{
                binding.rg1.clearCheck()
                colorCode = colorArr[7]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr8.setOnClickListener{
                binding.rg1.clearCheck()
                colorCode = colorArr[8]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }
            colorGr9.setOnClickListener{
                binding.rg1.clearCheck()
                colorCode = colorArr[9]
                rvGroup.setBackgroundColor(resources.getColor(colorCode, null))
            }

            val dlg = GroupDialog(this@GroupActivity)
            dlg.setOnClickedListener(object : GroupDialog.ButtonClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClicked(email: String) {
                    Log.e("IMECallback", email)
                    userArr.add(email)
                    Log.d("IMECallback", userArr.toString())
                    //binding.rvGroup.adapter!!.notifyItemInserted(binding.rvGroup.adapter!!.itemCount - 1)
                    binding.rvGroup.adapter?.notifyDataSetChanged()
                }
            })
            btnAddUser.setOnClickListener{
                dlg.show()
            }


//            val imgId = resources.getIdentifier(rg1.checkedRadioButtonId.toString(), "color" ,packageName)
//            val color = getColor(imgId);
//            val imgView = findViewById<ImageView>(imgId)
        }
    }

    private fun confirmGroup(){
        binding.apply {
            val email = ApplicationClass.sSharedPreferences.getString("user_id", null)
            val cal = Calendar(
                "0",
                etGroupName.text.toString(),
                etGroupInfo.text.toString(),
                colorCode,
                email,
                userArr
            )
            val calendar = Firebase.database.getReference("Calendar")
            calendar.push().setValue(cal)
        }
    }

    private fun initRV(){
        if(type == "fix"){
            val table = Firebase.database.getReference("Calendar")
            val query = table.orderByKey().equalTo("test1")

            val option = FirebaseRecyclerOptions.Builder<Calendar>()
                .setQuery(query, Calendar::class.java)
                .build()

            memberFBAdapter = MemberFBAdapter(option)
            binding.rvGroup.adapter = memberFBAdapter
            binding.rvGroup.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            memberFBAdapter.startListening()
        } else {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        if(type == "fix") memberFBAdapter.stopListening()
    }


}