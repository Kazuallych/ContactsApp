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
            val intent = Intent()
            intent.putExtra("nameAdd",binding.edName.text.toString())
            intent.putExtra("phoneAdd",binding.edPhone.text.toString())
            setResult(RESULT_OK,intent)
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }
    }

}