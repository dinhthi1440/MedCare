package com.example.medcare.views.user_manager.user_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemDoctorBinding
import com.example.medcare.databinding.ItemUserManagerBinding
import com.example.medcare.models.Account
import com.example.medcare.models.Doctor

class UserListAdapter (
    private val onView: (Account) -> Unit,
    private val onLock: (Account) -> Unit,
) : BaseAdapter<Account, BaseViewHolder<Account>>(Account.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Account> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemUserManagerBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemUserManagerBinding) :
        BaseViewHolder<Account>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Account, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtRelativeName.text = item.fullName
                root.setOnClickListener {
                    onView(item)
                }
                btnView.setOnClickListener {
                    onView(item)
                }
                btnLock.setOnClickListener {
                    onLock(item)
                }
            }

        }
    }
}