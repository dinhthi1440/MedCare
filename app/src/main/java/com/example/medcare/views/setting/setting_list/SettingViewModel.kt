package com.example.medcare.views.setting.setting_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.models.Account
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response
import com.example.medcare.utils.FolderS3
import com.example.medcare.utils.S3UploaderUtils
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SettingViewModel(private val iAuthRepository: IAuthRepository) : BaseViewModel() {
    private val _setUpdateStatus = MutableLiveData<String>()
    val getUpdateStatus: LiveData<String> get() = _setUpdateStatus

    private val _setChangePasswordStatus = MutableLiveData<String>()
    val getChangePasswordStatus: LiveData<String> get() = _setChangePasswordStatus

    val getUserStatus: LiveData<Response<Any>> get() = _setUserStatus
    private val _setUserStatus = MutableLiveData<Response<Any>>()

    fun updateInformation (account: Account, file: File?) {
        setIsLoading(true)
        viewModelScope.launch {
            if (file != null) {
                try {
                    val imageUrl = S3UploaderUtils.uploadFileToS3(FolderS3.USERS.folderName, file)
                    if (imageUrl != null) {
                        account.avatar = imageUrl
                    } else {
                        setIsLoading(false)
                        withContext(Dispatchers.Main) {
                            _messageError.value = "Có lỗi khi tải ảnh lên"
                        }
                        return@launch
                    }
                } catch (e: Exception) {
                    Log.e("S3Uploader", "Upload failed", e)
                    setIsLoading(false)
                    withContext(Dispatchers.Main) {
                        _messageError.value = "Có lỗi khi tải ảnh lên"
                    }
                    return@launch
                }
            }
            val result = iAuthRepository.updateUser(account)
            withContext(Dispatchers.Main) {
                when (result.statusCode) {
                    200 -> _setUpdateStatus.value = result.message
                    else -> _messageError.value = result.message
                }

                setIsLoading(false)
            }
        }
    }

    fun updatePassword(oldPass: String, newPass: String) {
        setIsLoading(true)
        val user = FirebaseAuth.getInstance().currentUser

        if (user == null || user.email.isNullOrEmpty()) {
            setIsLoading(false)
            _messageError.value = "Không tìm thấy người dùng hiện tại."
            return
        }

        val credential = EmailAuthProvider.getCredential(user.email!!, oldPass)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPass)
                    .addOnSuccessListener {
                        setIsLoading(false)
                        _setChangePasswordStatus.value = "Cập nhật mật khẩu thành công"
                    }
                    .addOnFailureListener {
                        setIsLoading(false)
                        _messageError.value = "Cập nhật thất bại, vui lòng thử lại"
                    }
            }
            .addOnFailureListener { e ->
                setIsLoading(false)
                _messageError.value = "Mật khẩu cũ không đúng"
            }
    }

    fun getUserDataByID(uid: String) {
        viewModelScope.launch {
            try {
                val response = iAuthRepository.getUserData(uid)
                when (response.statusCode) {
                    200 -> {
                        val user = response.data as? Account
                        if (user != null) {
                            if (user.status == "locked") {
                                _setUserStatus.value = Response(
                                    403,
                                    "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên.",
                                    null
                                )
                            } else {
                                _setUserStatus.value = Response(
                                    200,
                                    "Đăng nhập thành công",
                                    user
                                )
                            }
                        } else {
                            _setUserStatus.value = Response(
                                500,
                                "Không thể phân tích dữ liệu người dùng",
                                null
                            )
                        }
                    }
                    404 -> {
                        _setUserStatus.value = Response(
                            404,
                            "Không tìm thấy người dùng với UID này",
                            null
                        )
                    }
                    else -> {
                        _setUserStatus.value = Response(
                            response.statusCode,
                            response.message,
                            null
                        )
                    }
                }
                setIsLoading(false)
            } catch (e: Exception) {
                setIsLoading(false)
                _setUserStatus.value = Response(
                    500,
                    e.message ?: "Đã xảy ra lỗi không xác định",
                    null
                )
            }
        }
    }
}