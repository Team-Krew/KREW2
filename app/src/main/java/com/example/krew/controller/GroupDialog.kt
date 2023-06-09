package com.example.krew.controller

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.krew.databinding.GroupDialogBinding

class GroupDialog(private val context : AppCompatActivity) : Dialog(context){
    //val pattern1 = Regex("""^\d{2}/\d{2}\s\d{2}:\d{2}\s\d{0,3}(,\d{3})*원$""")
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
            onClickedListener.onClicked(str)

            binding.etGroupDialog.text.clear()
            binding.btnGroupDialog.isEnabled = false
            dismiss()
        }

        binding.btnCancel.setOnClickListener{
            binding.etGroupDialog.text.clear()
            dismiss()
        }
    }
}