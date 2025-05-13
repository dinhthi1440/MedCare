package com.example.medcare.views.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentLoginBinding
import com.example.medcare.views.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){
    override val viewModel by viewModel<AuthViewModel>()

    override fun initData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun handleEvent() {

        binding.txtSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.btnLogin.setOnClickListener {
            val emailInput = binding.textipEmail.text.toString().trim()
            val passwordInput = binding.textipPassword.text.toString().trim()
            val isCheckedRemember = binding.checkboxAgree.isChecked
            if (emailInput == "" || passwordInput == "") {
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show()
            } else {
                hideKeyboard(it)
                viewModel.loginWithEmailPassword(emailInput, passwordInput)
            }
        }
        binding.root.setOnTouchListener { it, _ ->
            hideKeyboard(it)
            true
        }
        binding.txtvForgotPassword.setOnClickListener {

        }
    }

    override fun bindData() {
        setFragmentResultListener("register_result") { _, bundle ->
            val email = bundle.getString("email")
            val password = bundle.getString("password")
            binding.textipEmail.setText(email)
            binding.textipPassword.setText(password)
        }
        viewModel.getLoginStatus.observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                sharedPreferences.saveUserID(it.data)
                findNavController().navigate(
                    R.id.action_loginFragment_to_homeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                )
            } else {
                Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun destroy() {

    }

}