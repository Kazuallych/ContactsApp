package com.example.contacts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
class MainActivity : ComponentActivity(),OnDeleteItem {
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var editLauncher: ActivityResultLauncher<Intent>? = null
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: Adapter
    lateinit var data: ArrayList<Contanct>
    lateinit var filteredList :ArrayList<Contanct>
    lateinit var contact: Contanct
    lateinit var db: MainDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var currentPosition = 0
        filteredList = ArrayList()

        data = ArrayList()
        db = MainDB.getDb(this)

        lifecycleScope.launch {
            data.addAll(ArrayList(db.getDao().getAllData()))

            adapter = Adapter({position,contact->
                val i = Intent(this@MainActivity, Shablon::class.java)
                currentPosition = position
                this@MainActivity.contact = contact
                i.putExtra("edName",data[data.indexOf(contact)].name)
                i.putExtra("edPhone",data[data.indexOf(contact)].phone)
                editLauncher?.launch(i)
            },data,this@MainActivity)

            binding.rcView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rcView.adapter = adapter
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult->
            if(result.resultCode== RESULT_OK){
                val newContact = Contanct(null,result.data?.getStringExtra("nameAdd").toString(),result.data?.getStringExtra("phoneAdd").toString())
                lifecycleScope.launch {
                    db.getDao().insert(newContact)
                    data.clear()
                    data.addAll(db.getDao().getAllData())

                    withContext(Dispatchers.Main){
                        adapter.notifyItemInserted(data.size-1)
                    }
                }
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

                lifecycleScope.launch {
                    val index = data.indexOf(contact)
                    data[index].name = result.data?.getStringExtra("nameAdd").toString()
                    data[index].phone = result.data?.getStringExtra("phoneAdd").toString()
                    db.getDao().update(data[index])
                    adapter.notifyItemChanged(currentPosition, adapter.itemCount)
                }
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
                }
//                }else if(i.phone.lowercase(Locale.ROOT).contains(query)){
//                    filteredList.add(i)
//                }
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
        lifecycleScope.launch {
            db.getDao().delete(item)
            if(filteredList.isNotEmpty()){
                filteredList.removeAt(position)
            }
            val index = data.indexOf(item)
            data.removeAt(index)
            adapter.notifyItemRemoved(position)
        }
    }

}