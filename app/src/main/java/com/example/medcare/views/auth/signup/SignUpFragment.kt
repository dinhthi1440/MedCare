package com.example.medcare.views.auth.signup

import android.os.Bundle
import android.provider.Settings.Global.putString
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSignUpBinding
import com.example.medcare.views.auth.AuthViewModel
//import org.checkerframework.checker.regex.qual.Regex
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {
    var email = ""
    var password = ""
    override val viewModel by viewModel<AuthViewModel>()

    override fun initData() {

    }


    override fun handleEvent() {
        binding.txtLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.root.setOnClickListener {
            hideKeyboard(it)
            true
        }
        binding.btnSignUp.setOnClickListener {
            val emailInput = binding.textipEmail.text.toString().trim()
            val passwordInput = binding.textipPassword.text.toString().trim()
            val confirmPasswordInput = binding.textipPasswordConfirm.text.toString().trim()

            val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

            val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")

            if (!emailInput.matches(emailPattern)) {
                binding.textipEmail.error = "Email không hợp lệ"
                return@setOnClickListener
            }

            if (!passwordInput.matches(passwordPattern)) {
                binding.textipPassword.error = "Mật khẩu phải từ 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
                return@setOnClickListener
            }

            if (passwordInput != confirmPasswordInput) {
                binding.textipPasswordConfirm.error = "Mật khẩu không khớp"
                return@setOnClickListener
            }

            viewModel.registerAccount(emailInput, passwordInput)
            email = emailInput
            password = passwordInput
        }

    }

    override fun bindData() {
        viewModel.getSignUpStatus.observe(viewLifecycleOwner) {
            if (it.statusCode == 201){
                val result = Bundle().apply {
                    putString("email", email)
                    putString("password", password)
                }
                setFragmentResult("register_result", result)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun destroy() {

    }
}