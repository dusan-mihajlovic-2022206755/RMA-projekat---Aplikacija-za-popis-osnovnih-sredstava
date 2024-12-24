package com.example.popisosnovnihsredstava.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.popisosnovnihsredstava.R

class PregledStavkiAdapter(private val items: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    var filteredItems: List<String> = items

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = filteredItems[position]
    }

    override fun getItemCount(): Int = filteredItems.size

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter { it.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}
