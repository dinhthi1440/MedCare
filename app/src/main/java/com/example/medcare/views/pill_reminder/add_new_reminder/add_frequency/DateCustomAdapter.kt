package com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medcare.databinding.ItemSelectDateBinding

class DateCustomAdapter(
    private val days: MutableList<DateCustom>,
    private val daysSelected: MutableList<DateCustom>,
    private val onItemSelected: (DateCustom, Boolean) -> Unit
) : RecyclerView.Adapter<DateCustomAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSelectDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: DateCustom) {
            binding.tvDay.text = day.label
            binding.btnToggle.isChecked = daysSelected.any { it.abbreviation == day.abbreviation }
            binding.btnToggle.setOnCheckedChangeListener { _, isChecked ->
                onItemSelected(day, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount() = days.size
}