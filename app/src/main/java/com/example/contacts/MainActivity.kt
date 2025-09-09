package com.example.contacts

import OnDeleteItem
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import com.example.contacts.ui.theme.ContactsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(),OnDeleteItem {
    private var launcher: ActivityResultLauncher<Intent>? = null
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var data: ArrayList<Contanct>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        data = ArrayList()
        data.add(Contanct("123","123"))
        adapter = Adapter(data,this)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            val newContact = Contanct(result.data?.getStringExtra("nameAdd").toString(),result.data?.getStringExtra("phoneAdd").toString())
            data.add(newContact)
            adapter.notifyItemInserted(data.size-1)
        }

        binding.btAdd.setOnClickListener {
            launcher?.launch(Intent(this@MainActivity, Shablon::class.java))
        }
    }
    override fun onDeleteItem(position: Int) {
        data.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position,adapter.itemCount-position)
    }

}