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
import com.example.medcare.extension.getData
import com.example.medcare.extension.saveData
import com.example.medcare.models.Account
import com.example.medcare.utils.Constants
import com.example.medcare.views.auth.AuthViewModel
import com.firebase.ui.auth.data.model.User
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){
    var initEmail = ""
    var initPassword = ""
    override val viewModel by viewModel<AuthViewModel>()

    override fun initData() {
        initEmail = sharedPreferences.getData(Constants.SHARED_EMAIL)
        initPassword = sharedPreferences.getData(Constants.SHARED_PASSWORD)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun handleEvent() {

        binding.txtSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.btnLogin.setOnClickListener {
            val emailInput = binding.textipEmail.text.toString().trim()
            val passwordInput = binding.textipPassword.text.toString().trim()
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
        if (initEmail != "" && initPassword != "") {
            binding.textipEmail.setText(initEmail)
            binding.textipPassword.setText(initPassword)
            viewModel.loginWithEmailPassword(initEmail, initPassword)
        }
        setFragmentResultListener("register_result") { _, bundle ->
            val email = bundle.getString("email")
            val password = bundle.getString("password")
            binding.textipEmail.setText(email)
            binding.textipPassword.setText(password)
        }
        viewModel.getUserStatus.observe(viewLifecycleOwner) {
            when (it.statusCode) {
                200 -> {
                    val user = it.data as Account
                    val json = gson.toJson(user)
                    sharedPreferences.saveData(json, Constants.SHARED_USER)
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, true)
                            .build()
                    )
                }
                404 -> {
                    findNavController().navigate(
                        R.id.action_loginFragment_to_onBoardingInforFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.nav_graph, true)
                            .build()
                    )
                }
            }
        }
        viewModel.getLoginStatus.observe(viewLifecycleOwner) {
            if (it.statusCode == 200) {
                val isCheckedRemember = binding.cbSaveLogin.isChecked
                if (isCheckedRemember) {
                    val emailInput = binding.textipEmail.text.toString().trim()
                    val passwordInput = binding.textipPassword.text.toString().trim()
                    sharedPreferences.saveData(emailInput, Constants.SHARED_EMAIL)
                    sharedPreferences.saveData(passwordInput, Constants.SHARED_PASSWORD)
                }
                sharedPreferences.saveData(it.data.toString(), Constants.SHARED_USER_ID)
                viewModel.getUserDataByID(it.data.toString())
            } else {
                Toast.makeText(context, "login ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun destroy() {

    }

}