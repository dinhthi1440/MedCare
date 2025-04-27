package com.example.medcare.views.pill_reminder.add_new_reminder.select_medicines

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemSelectMedicineBinding
import com.example.medcare.models.Medicine

class SelectMedicineAdapter(
    private val initialSelectedList: MutableSet<Medicine>,
    private val onClickMedicine: (Medicine) -> Unit,
    private val onUnSelect: (Medicine) -> Unit
) : BaseAdapter<Medicine, BaseViewHolder<Medicine>>(Medicine.differUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Medicine> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectMedicineBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemSelectMedicineBinding) :
        BaseViewHolder<Medicine>(binding) {

        @SuppressLint("SetTextI18n")
        override fun bindView(item: Medicine, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtMedicineName.text = item.name
                txtDosageInput.text = "\uD83D\uDD22 Liều lượng: 3 viên"
                cbSelectMedicine.isChecked = initialSelectedList.any { it.id == item.id }
                cbSelectMedicine.setOnCheckedChangeListener(null)
                imageView.setImageURI(item.image.toUri())
                cbSelectMedicine.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        onClickMedicine(item)
                    } else {
                        onUnSelect(item)
                    }
                }
            }
        }
    }
}