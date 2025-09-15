package com.example.contacts

import OnDeleteItem
import android.content.Intent
import android.os.Bundle
import android.provider.Contacts
import android.provider.ContactsContract
import android.util.Log
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
    lateinit var filteredList :ArrayList<Contanct>
    lateinit var contact: Contanct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var currentPosition = 0
        filteredList = ArrayList()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList()
        adapter = Adapter({position,contact->
            val i = Intent(this, Shablon::class.java)
            currentPosition = position
            this.contact = contact
            i.putExtra("edName",data[data.indexOf(contact)].name)
            i.putExtra("edPhone",data[data.indexOf(contact)].phone)
            editLauncher?.launch(i)
        },data,this)

        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            if(result.resultCode== RESULT_OK){
                var id = 0
                if(data.size==0){
                    id=0
                }else {
                    id = data[data.size-1].id+1
                }
                val newContact = Contanct(result.data?.getStringExtra("nameAdd").toString(),result.data?.getStringExtra("phoneAdd").toString(),id)
                data.add(newContact)
                adapter.notifyItemInserted(data.size-1)
            }
        }
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            if(result.resultCode ==RESULT_OK){
                if(filteredList.isNotEmpty()) {
                    filteredList[currentPosition].name = result.data?.getStringExtra("nameAdd").toString()
                    filteredList[currentPosition].phone = result.data?.getStringExtra("phoneAdd").toString()
                    Log.d("MyLog","Отредактировано")
                }
                val index = data.indexOf(contact)
                data[index].name = result.data?.getStringExtra("nameAdd").toString()
                data[index].phone = result.data?.getStringExtra("phoneAdd").toString()
                adapter.notifyItemChanged(currentPosition, adapter.itemCount)
            }
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
            for(i in data){
                if(i.name.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                    Log.d("MyLog","${filteredList.size}")
                }
            }
            if(query==""){
                filteredList = data
                adapter.setFilteredData(filteredList)
                filteredList = ArrayList()
            }
            if(filteredList.isEmpty()){
                Toast.makeText(this,"Ничего нет", Toast.LENGTH_SHORT).show()
            }else{
                adapter.setFilteredData(filteredList)
            }
        }
    }
    override fun onDeleteItem(item: Contanct,position:Int) {
        if(filteredList.isNotEmpty()){
            filteredList.removeAt(position)
        }
        val index = data.indexOf(item)
        data.removeAt(index)
        adapter.notifyItemRemoved(position)
    }

}