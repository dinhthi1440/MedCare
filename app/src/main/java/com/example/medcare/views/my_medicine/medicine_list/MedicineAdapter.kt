package com.example.medcare.views.my_medicine.medicine_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemMedicineRowBinding
import com.example.medcare.views.my_medicine.medicine_list.model.Medicine

class MedicineAdapter(private val onClick: (Medicine) -> Unit) : BaseAdapter<Medicine, BaseViewHolder<Medicine>>(
    Medicine.differUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Medicine> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemMedicineRowBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemMedicineRowBinding): BaseViewHolder<Medicine>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Medicine, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtMedicineName.text = item.name
                txtExpirationDate.text = "${R.string.expiration_date} ${item.expirationDate}"
                txtQuantity.text = "${R.string.quantity} ${item.quantity} ${item.unit}"
                root.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }
}