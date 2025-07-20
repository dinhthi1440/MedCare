package com.example.medcare.views.my_medicine.medicine_list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemMedicineRowBinding
import com.example.medcare.models.Medicine

class MedicineAdapter(private val isSelectMedicine: Boolean, private val onClick: ((Medicine) -> Unit)?) : BaseAdapter<Medicine, BaseViewHolder<Medicine>>(
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
            if (item.image != "") {
                binding.imageViewNoImage.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                Glide.with(binding.root.context)
                    .load(item.image)
                    .error(R.drawable.error_image)
                    .into(binding.imageView)
            } else {
                binding.imageViewNoImage.visibility = View.VISIBLE
                binding.imageView.visibility = View.INVISIBLE
            }
            if (item.creatorID != "") {
                binding.txtMedicineName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.ccRedText))
            }
            if (!isSelectMedicine) {
                binding.apply {
                    txtExpirationDate.visibility = View.GONE
                    txtQuantity.visibility = View.GONE
                    txtMedicineName.text = item.name

                    txtDosageInput.visibility = View.VISIBLE
                    txtDosageInput.text = "\uD83D\uDD22 Liều lượng: ${item.dosage} ${item.unit}"
                 }
            } else {
                binding.apply {
                    txtMedicineName.text = item.name
                    txtExpirationDate.text = "⏳ HSD: ${item.expirationDate}"
                    txtQuantity.text = "\uD83D\uDD22 SL còn: ${item.quantity} ${item.unit}"
                    root.setOnClickListener {
                        onClick?.invoke(item)
                    }
                }
            }

        }
    }
}