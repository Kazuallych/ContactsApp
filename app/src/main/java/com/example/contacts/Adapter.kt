package com.example.contacts

import OnDeleteItem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val onEditClick: (Int) -> Unit,var data: ArrayList<Contanct>,private val delete:OnDeleteItem): RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvName = view.findViewById<TextView>(R.id.tvName)
        var tvPhome = view.findViewById<TextView>(R.id.tvPhone)

        var btDel = view.findViewById<Button>(R.id.btDel)
    }

    fun setFilteredData(data: ArrayList<Contanct>){

        this.data = data
        Log.d("MyLog","хуйнфя сработала")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shablon_main,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[holder.bindingAdapterPosition]
        holder.tvName.text = item.name
        holder.tvPhome.text = item.phone
        // удаление элемента
        holder.btDel.setOnClickListener {
            delete.onDeleteItem(holder.bindingAdapterPosition)
        }
        //Запуск редактирования
        holder.itemView.setOnClickListener {
            onEditClick(holder.bindingAdapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}