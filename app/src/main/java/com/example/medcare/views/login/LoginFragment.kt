package com.example.medcare.views.login

import android.view.LayoutInflater
import com.example.medcare.base.BaseFragment
import com.example.medcare.base.BaseViewModel
import com.example.medcare.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate){
    override val viewModel by viewModel<AuthViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {

    }

    override fun bindData() {

    }

    override fun destroy() {

    }

}