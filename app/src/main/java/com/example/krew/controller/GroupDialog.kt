package com.example.krew.controller

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.krew.databinding.GroupDialogBinding
import com.example.krew.model.TempUser
import com.example.krew.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupDialog(private val context : AppCompatActivity) : Dialog(context){
    val pattern1 = Regex("""\w+@\w+.\w+$""")

    private lateinit var binding : GroupDialogBinding

    interface ButtonClickListener {
        fun onClicked(myName: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        binding = GroupDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)     //다이얼로그에 사용할 xml 파일을 불러옴
        init()
    }

    fun init() {
        setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.etGroupDialog.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(pattern1.containsMatchIn(binding.etGroupDialog.text.toString())){
                    binding.btnGroupDialog.isEnabled = true;
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnGroupDialog.setOnClickListener{
            //email로 딥링크 보내기, group 관련 정보 같이 내보내기
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            val str = binding.etGroupDialog.text.toString()
            var isAvailable = false
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("User")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(TempUser::class.java)
                        Log.e("Firebase Communication", "checking validity => ${user!!.user_email}==${str}")
                        if(user.user_email == str) {
                            isAvailable = true
                            Log.e("Firebase Communication", "Fucking Available")
                            break;
                        }
                    }

                    if(isAvailable){
                        onClickedListener.onClicked(str)
                        binding.etGroupDialog.text.clear()
                        binding.btnGroupDialog.isEnabled = false
                        dismiss()
                    } else{
                        Log.e("Firebase Communication", "Fucking not Available")
                        Toast.makeText(context, "가입되지 않은 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase Communication", "error!!")
                }
            })



        }

        binding.btnCancel.setOnClickListener{
            binding.etGroupDialog.text.clear()
            dismiss()
        }
    }

}