package com.example.medcare.views.user_manager.user_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.models.Account
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserListViewModel(private val iAuthRepository: IAuthRepository) : BaseViewModel() {

    val getAccounts: LiveData<MutableList<Account>> get() = _setAccounts
    private val _setAccounts = MutableLiveData<MutableList<Account>>()

    val getAccountDetail: LiveData<Account> get() = _setAccountDetail
    private val _setAccountDetail = MutableLiveData<Account>()

    private val _setUpdateStatus = MutableLiveData<String>()
    val getUpdateStatus: LiveData<String> get() = _setUpdateStatus

    private val _setDeleteStatus = MutableLiveData<String>()
    val getDeleteStatus: LiveData<String> get() = _setDeleteStatus


    fun getAccountList(searchName: String) {
        executeTask(
            request = {
                if (searchName.isBlank()){
                    iAuthRepository.getAllUser()
                } else {
                    iAuthRepository.searchUserByName(searchName)
                }
            },
            onSuccess = {
               when (it.statusCode) {
                   200 -> {
                       val listAccount = it.data as List<Account>
                       _setAccounts.value = listAccount.toMutableList()
                   }
                   204, 500 -> {
                       _messageError.value = it.message
                   }
               }
            },
            onError = {
                _messageError.value = "Lỗi khi lấy dữ liệu"
            }
        )

    }

    fun getUserByID(accountID: String) {
        viewModelScope.launch {
            setIsLoading(true)
            val result = iAuthRepository.getUserData(accountID)
            when (result.statusCode) {
                200 -> {
                    val accountDetail = result.data as Account
                    _setAccountDetail.value = accountDetail
                    setIsLoading(false)
                }
                204, 500 -> {
                    _messageError.value = result.message
                    setIsLoading(false)
                }
            }
        }
    }
    fun updateUser(accountID: String, fields: Map<String, Any>) {
        viewModelScope.launch {
            setIsLoading(true)
            val result = iAuthRepository.updateUserByFiled(accountID, fields)
            withContext(Dispatchers.Main) {
                when (result.statusCode) {
                    200 -> {
                        _setUpdateStatus.value = fields["rule"].toString()
                        _messageError.value = result.message
                    }
                    else -> _messageError.value = result.message
                }
                setIsLoading(false)
            }
        }
    }

    fun deleteUser(accountID: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                setIsLoading(true)
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                try {
                    val credential = EmailAuthProvider.getCredential(email, password)
                    currentUser?.reauthenticate(credential)?.await()
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    _messageError.value = "Mật khẩu không đúng. Vui lòng thử lại."
                    setIsLoading(false)
                    return@launch
                } catch (e: FirebaseAuthException) {
                    _messageError.value = "Xác thực thất bại: ${e.message}"
                    setIsLoading(false)
                    return@launch
                }

                val result = iAuthRepository.deleteUserByID(accountID)
                when (result.statusCode) {
                    200 -> {
                        _messageError.value = result.message
                        _setDeleteStatus.value = "Success"
                    }
                    404 , 500 -> {
                        _messageError.value = result.message
                    }
                }
                setIsLoading(false)
            } catch (e: Exception) {
                setIsLoading(false)
                _messageError.value = "Lỗi không xác định"
            }
        }
    }
}