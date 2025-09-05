package com.example.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.contacts.databinding.ShablonBinding

class Shablon: ComponentActivity() {
    lateinit var binding: ShablonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ShablonBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}