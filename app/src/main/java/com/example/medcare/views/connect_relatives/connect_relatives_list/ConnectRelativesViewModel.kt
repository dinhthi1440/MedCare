package com.example.medcare.views.connect_relatives.connect_relatives_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.models.Account
import com.example.medcare.models.Relative

class ConnectRelativesViewModel(private val iRelativeRepos: IRelativeRepos) : BaseViewModel() {
    private val _setRelativeList = MutableLiveData<MutableList<Relative>>()
    val getRelativeList: LiveData<MutableList<Relative>> get() = _setRelativeList

    private val _setAddRequestList = MutableLiveData<MutableList<Relative>>()
    val getAddRequestList: LiveData<MutableList<Relative>> get() = _setAddRequestList

    private val _setSearchList = MutableLiveData<MutableList<Relative>>()
    val getSearchList: LiveData<MutableList<Relative>> get() = _setSearchList

    private val _acceptStatus = MutableLiveData<Boolean>()
    val acceptStatus: LiveData<Boolean> get() = _acceptStatus

    fun getRelatives() {

    }

    fun getSearchRelatives(searchString: String) {
        executeTask(
            request = {iRelativeRepos.getSearchRelativesRemote(searchString)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val data = it.data as List<Relative>
                        _setSearchList.value = data.toMutableList()
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi khi tìm kiếm"
            }
        )
    }

    fun insertRelativesRequestRemote(uid: String, relative: Relative) {
        executeTask(
            request = {iRelativeRepos.insertRelativesRequestRemote(uid, relative)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _messageError.value = it.message
                    }
                    409, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi khi tìm kiếm"
            }
        )
    }

    fun getAllRelativeRequestRemote(uid: String) {
        executeTask(
            request = {iRelativeRepos.getAllRelativeRequestRemote(uid)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listRequest = it.data as List<Relative>
                        _setAddRequestList.value = listRequest.toMutableList()
                    }
                    204 -> {
                        _setAddRequestList.value = mutableListOf()
                    }
                    500 -> {
                        //_messageError.value = it.message
                    }
                }
            },
            onError = {
                //_messageError.value = "Lỗi khi tìm kiếm"
            }
        )
    }

    fun acceptRelativeRequestRemote( user: Account, relative: Relative ) {
        executeTask(
            request = {iRelativeRepos.acceptRelativeRequestRemote(user, relative)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _acceptStatus.value = true
                    }
                    404, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi chấp nhận"
            }
        )
    }

    fun getAllRelativesRemote(uid: String) {
        executeTask(
            request = {iRelativeRepos.getAllRelativesRemote(uid)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val relativeList = it.data as List<Relative>
                        _setRelativeList.value = relativeList.toMutableList()
                    }
                    204 -> {
                        _setRelativeList.value = mutableListOf()
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {

            }
        )
    }
 }