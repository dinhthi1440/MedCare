package com.example.medcare.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medcare.R
import com.example.medcare.views.home.model.MenuItem

class MenuAdapter(private val items: List<MenuItem>, val onClickItem: (MenuItem) -> Unit) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgIcon: TextView = view.findViewById(R.id.imgIcon)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = items[position]
        holder.imgIcon.text = item.icon
        holder.txtTitle.text = item.title
        holder.itemView.setOnClickListener {
            onClickItem(item)
        }
    }

    override fun getItemCount(): Int = items.size
}