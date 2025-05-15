package com.example.medcare.views.setting.setting_list

import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSettingListBinding
import com.example.medcare.extension.saveData
import com.example.medcare.utils.Constants


class SettingListFragment : BaseFragment<FragmentSettingListBinding>(FragmentSettingListBinding::inflate) {

    override val viewModel by viewModel<SettingViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            layoutLogOut.setOnClickListener {
                sharedPreferences.saveData("", Constants.SHARED_EMAIL)
                sharedPreferences.saveData("", Constants.SHARED_PASSWORD)
                sharedPreferences.saveData("", Constants.SHARED_USER_ID)
                findNavController().navigate(R.id.action_settingListFragment_to_loginFragment, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }
        }

    }

    override fun bindData() {

    }

    override fun destroy() {

    }
}