package com.example.medcare.views.user_manager.user_detail

import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentUserDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserDetailFragment : BaseFragment<FragmentUserDetailBinding>(FragmentUserDetailBinding::inflate) {
    override val viewModel by viewModel<UserDetailViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {

    }

    override fun bindData() {

    }

    override fun destroy() {

    }
}