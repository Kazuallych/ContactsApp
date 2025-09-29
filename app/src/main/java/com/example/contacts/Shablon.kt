package com.example.contacts

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.contacts.databinding.ShablonBinding

class Shablon: ComponentActivity() {
    lateinit var binding: ShablonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ShablonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //занос данных при редактировании
        if(binding.edName.text.toString() =="" &&binding.edPhone.text.toString()==""){
            binding.edName.setText(intent.getStringExtra("edName"))
            binding.edPhone.setText(intent.getStringExtra("edPhone"))
        }

        binding.btDone.setOnClickListener {
            if(binding.edPhone.text.toString()==""){
                binding.edPhone.error = "Не корректный номер телефона"
                return@setOnClickListener
            }
            if((binding.edPhone.text[0] =='7'||binding.edPhone.text[0] =='8')&&binding.edPhone.text.length==11) {
                val intent = Intent()
                intent.putExtra("nameAdd", binding.edName.text.toString())
                intent.putExtra("phoneAdd", binding.edPhone.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }else if (binding.edPhone.text[0]=='+'&&(binding.edPhone.text[1]=='7'||binding.edPhone.text[1]=='8')&&binding.edPhone.text.length==12 ) {
                val intent = Intent()
                intent.putExtra("nameAdd", binding.edName.text.toString())
                intent.putExtra("phoneAdd", binding.edPhone.text.toString())
                setResult(RESULT_OK, intent)
                finish()
            }else{
                binding.edPhone.error = "Не корректный номер телефона"
            }
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }

}