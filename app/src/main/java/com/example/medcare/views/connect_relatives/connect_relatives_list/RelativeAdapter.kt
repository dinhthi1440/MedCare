package com.example.medcare.views.connect_relatives.connect_relatives_list

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemRelativeBinding
import com.example.medcare.models.Relative

class RelativeAdapter(
    private val onView: (Relative) -> Unit,
    private val onCreateReminder: (relative :Relative) -> Unit,
    private val onRemove: (Relative) -> Unit
) : BaseAdapter<Relative, BaseViewHolder<Relative>>(Relative.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Relative> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemRelativeBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemRelativeBinding) :
        BaseViewHolder<Relative>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Relative, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                if (item.avatar != "") {
                    Glide.with(binding.root.context)
                        .load(item.avatar)
                        .error(R.drawable.error_image)
                        .into(binding.imgUser)
                }
                txtRelativeName.text = item.fullName
                txtTitleRelative.text = item.relativeTitle
                btnView.setOnClickListener {
                    onView(item)
                }
                btnCreateReminder.setOnClickListener {
                    onCreateReminder(item)
                }
                root.setOnLongClickListener {
                    onRemove(item)
                    true
                }
            }

        }
    }
}