package com.example.medcare.views.setting.setting_list

import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSettingListBinding


class SettingListFragment : BaseFragment<FragmentSettingListBinding>(FragmentSettingListBinding::inflate) {

    override val viewModel by viewModel<SettingViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {

    }

    override fun destroy() {

    }
}