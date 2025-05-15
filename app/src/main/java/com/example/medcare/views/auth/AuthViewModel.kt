package com.example.medcare.views.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.models.Account
import com.example.medcare.models.Doctor
import com.example.medcare.models.Response

class AuthViewModel(private val iAuthRepository: IAuthRepository.Remote): BaseViewModel() {
    val getSignUpStatus: LiveData<Response<Any>> get() = _setSignUpStatus
    private val _setSignUpStatus = MutableLiveData<Response<Any>>()

    val getLoginStatus: LiveData<Response<Any>> get() = _setLoginStatus
    private val _setLoginStatus = MutableLiveData<Response<Any>>()

    val getUserStatus: LiveData<Response<Any>> get() = _setUserStatus
    private val _setUserStatus = MutableLiveData<Response<Any>>()


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

    fun getUserDataByID(uid: String) {
        executeTask(
            request = { iAuthRepository.getUserData(uid) },
            onSuccess = { response ->
                when (response.statusCode) {
                    200 -> {
                        val user = response.data as? Account
                        if (user != null) {
                            if (user.status == "locked") {
                                _setLoginStatus.value = Response(
                                    403,
                                    "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên.",
                                    null
                                )
                            } else {
                                _setLoginStatus.value = Response(
                                    200,
                                    "Đăng nhập thành công",
                                    user
                                )
                            }
                        } else {
                            _setLoginStatus.value = Response(
                                500,
                                "Không thể phân tích dữ liệu người dùng",
                                null
                            )
                        }
                    }
                    404 -> {
                        _setLoginStatus.value = Response(
                            404,
                            "Không tìm thấy người dùng với UID này",
                            null
                        )
                    }
                    else -> {
                        _setLoginStatus.value = Response(
                            response.statusCode,
                            response.message,
                            null
                        )
                    }
                }
            },
            onError = {
                _setLoginStatus.value = Response(
                    500,
                    it.message ?: "Đã xảy ra lỗi không xác định",
                    null
                )
            }
        )
    }

}