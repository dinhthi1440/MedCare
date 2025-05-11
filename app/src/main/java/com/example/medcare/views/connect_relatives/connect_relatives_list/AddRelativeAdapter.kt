package com.example.medcare.views.connect_relatives.connect_relatives_list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemRelativeBinding
import com.example.medcare.databinding.ItemRequestAddRelativeBinding
import com.example.medcare.models.Relative

class AddRelativeAdapter(
    private val onClick: (Relative) -> Unit,
    private val onAddRelative: (String) -> Unit,
) : BaseAdapter<Relative, BaseViewHolder<Relative>>(Relative.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Relative> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemRequestAddRelativeBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemRequestAddRelativeBinding) :
        BaseViewHolder<Relative>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Relative, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtRelativeName.text = item.name
                txtDescription.text = "Tôi là ${item.relativeTitle} của bạn, hãy chấp nhận kết nối!"
                root.setOnClickListener {
                    onClick(item)
                }
                btnAddRelative.setOnClickListener {
                    onAddRelative(item.id)
                }
            }

        }
    }
}