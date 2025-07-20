package com.example.medcare.views.setting.setting_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.medcare.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSettingListBinding
import com.example.medcare.extension.getData
import com.example.medcare.extension.saveData
import com.example.medcare.extension.showImage
import com.example.medcare.models.Account
import com.example.medcare.utils.Constants


class SettingListFragment : BaseFragment<FragmentSettingListBinding>(FragmentSettingListBinding::inflate) {

    override val viewModel by viewModel<SettingViewModel>()

    private var account: Account? = null
    override fun initData() {
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)

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
                sharedPreferences.saveData("", Constants.SHARED_USER)
                findNavController().navigate(R.id.action_settingListFragment_to_loginFragment, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }
            layoutInforUser.setOnClickListener {
                findNavController().navigate(R.id.action_settingListFragment_to_settingInformationFragment)
            }
            layoutNotificationSetting.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("rule", account?.rule)
                }
                findNavController().navigate(R.id.action_settingListFragment_to_settingNotificationFragment, bundle)
            }
            layoutChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_settingListFragment_to_changePassFragment)
            }
            imgAvtUser.setOnClickListener {
                if (account?.avatar != null) {
                    dialog(requireContext()).showImage(account?.avatar ?:"")
                }
            }
            layoutFeedback.setOnClickListener {
                findNavController().navigate(R.id.action_settingListFragment_to_settingFeedbackFragment)
            }
        }

    }
    private fun bindUserData() {
        binding.apply {
            if (account?.rule == "admin") {
                layoutFeedback.visibility = View.GONE
            }
            txtvUsername.text = account?.fullName ?: ""
            if (account?.avatar != "") {
                Glide.with(requireContext())
                    .load(account?.avatar)
                    .into(imgAvtUser)
            }
        }
    }

    override fun bindData() {
        bindUserData()
        listenBackScreen {
            viewModel.getUserDataByID(uid)
        }
        viewModel.getUserStatus.observe(viewLifecycleOwner) {
            when (it.statusCode) {
                200 -> {
                    val user = it.data as Account
                    val json = gson.toJson(user)
                    account = user
                    sharedPreferences.saveData(json, Constants.SHARED_USER)
                    bindUserData()
                }
                404, 500 -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun destroy() {

    }
}