package com.example.contacts

import OnDeleteItem
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import java.util.Locale


class MainActivity : ComponentActivity(),OnDeleteItem {
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var editLauncher: ActivityResultLauncher<Intent>? = null
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var data: ArrayList<Contanct>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currentPosition = 0

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList()
        data.add(Contanct("123","123"))
        adapter = Adapter({position->
            val i = Intent(this, Shablon::class.java)
            i.putExtra("edName",data[position].name)
            i.putExtra("edPhone",data[position].phone)
            currentPosition = position
            editLauncher?.launch(i)
        },data,this)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            val newContact = Contanct(result.data?.getStringExtra("nameAdd").toString(),result.data?.getStringExtra("phoneAdd").toString())
            data.add(newContact)
            adapter.notifyItemInserted(data.size-1)
        }
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            data[currentPosition].name = result.data?.getStringExtra("nameAdd").toString()
            data[currentPosition].phone = result.data?.getStringExtra("phoneAdd").toString()
            adapter.notifyItemChanged(currentPosition, adapter.itemCount)
        }

        binding.btAdd.setOnClickListener {
            launcher?.launch(Intent(this@MainActivity, Shablon::class.java))
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

        })
    }
    private fun filterList(query: String?){
        if(query != null){
            var filteredList = ArrayList<Contanct>()
            for(i in data){
                if(i.name.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
                Toast.makeText(this,"Ничего нет", Toast.LENGTH_SHORT).show()
            }else{
                if(query ==""){
                    filteredList = data
                    adapter.setFilteredData(filteredList)
                }else{
                    adapter.setFilteredData(filteredList)
                }
            }
        }

    }
    override fun onDeleteItem(position: Int) {
        data.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position,adapter.itemCount-position)
    }

}