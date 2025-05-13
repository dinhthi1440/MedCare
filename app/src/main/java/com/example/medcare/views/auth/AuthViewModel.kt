package com.example.medcare.views.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.models.Doctor
import com.example.medcare.models.Response

class AuthViewModel(private val iAuthRepository: IAuthRepository.Remote): BaseViewModel() {
    val getSignUpStatus: LiveData<Response<Any>> get() = _setSignUpStatus
    private val _setSignUpStatus = MutableLiveData<Response<Any>>()

    val getLoginStatus: LiveData<Response<Any>> get() = _setLoginStatus
    private val _setLoginStatus = MutableLiveData<Response<Any>>()
    fun registerAccount(email: String, password: String){
        executeTask(
            request = {iAuthRepository.registerAccount(email, password)},
            onSuccess = {
                _setSignUpStatus.value = it
            },
            onError = {
                _setSignUpStatus.value = Response(500, it.message.toString())
            }
        )
    }

    fun loginWithEmailPassword(email: String, password: String){
        executeTask(
            request = {iAuthRepository.loginWithEmailPassword(email, password)},
            onSuccess = {
                _setLoginStatus.value = it
            },
            onError = {
                _setLoginStatus.value = Response(500, it.message.toString())
            }
        )
    }
}