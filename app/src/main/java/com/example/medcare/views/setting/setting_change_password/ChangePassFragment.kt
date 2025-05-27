package com.example.medcare.views.setting.setting_change_password

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentChangePassBinding
import com.example.medcare.databinding.FragmentSettingNotificationBinding
import com.example.medcare.extension.saveData
import com.example.medcare.utils.Constants
import com.example.medcare.views.setting.setting_list.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePassFragment : BaseFragment<FragmentChangePassBinding>(
    FragmentChangePassBinding::inflate) {

    override val viewModel by viewModel<SettingViewModel>()

    override fun initData() {

    }


    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            root.setOnClickListener {
                hideKeyboard(it)
            }
            btnSignUp.setOnClickListener {
                val oldPass = textipOldPass.text.toString()
                val newPass = textipPassword.text.toString()
                val confirmPass = textipPasswordConfirm.text.toString()
                if (oldPass.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
                    Toast.makeText(context, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

                if (!newPass.matches(passwordPattern)) {
                    Toast.makeText(context, "Mật khẩu phải từ 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (newPass != confirmPass) {
                    Toast.makeText(context, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                hideKeyboard(it)
                viewModel.updatePassword(oldPass, newPass)

            }
        }

    }

    override fun bindData() {
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.textipOldPass.setText("")
            binding.textipPassword.setText("")
            binding.textipPasswordConfirm.setText("")
        }

        viewModel.getChangePasswordStatus.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            sharedPreferences.saveData("", Constants.SHARED_EMAIL)
            sharedPreferences.saveData("", Constants.SHARED_PASSWORD)
            sharedPreferences.saveData("", Constants.SHARED_USER_ID)
            sharedPreferences.saveData("", Constants.SHARED_USER)
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_changePassFragment_to_loginFragment2,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build()
                )
            }, 1500)
        }
    }
    override fun destroy() {

    }
}