package com.example.contacts

import OnDeleteItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val data: ArrayList<Contanct>,private val delete:OnDeleteItem): RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var tvName = view.findViewById<TextView>(R.id.tvName)
        var tvPhome = view.findViewById<TextView>(R.id.tvPhone)

        var btDel = view.findViewById<Button>(R.id.btDel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shablon_main,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.tvName.text = item.name
        holder.tvPhome.text = item.phone

        holder.btDel.setOnClickListener {
            delete.onDeleteItem(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}