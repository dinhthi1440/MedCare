package com.example.medcare.views.user_manager.user_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.Account
import com.example.medcare.models.ReminderHistory

class UserListViewModel : BaseViewModel() {
    val accounts = listOf(
        Account(
            id = "acc001",
            fullName = "Nguyễn Văn A",
            userName = "nguyenvana",
            email = "vana@example.com",
            avatar = "https://example.com/avatar/a.jpg",
            password = "hashed_pw_001",
            rule = "user",
            status = "active"
        ),
        Account(
            id = "acc002",
            fullName = "Trần Thị B",
            userName = "tranthib",
            email = "thib@example.com",
            avatar = "https://example.com/avatar/b.jpg",
            password = "hashed_pw_002",
            rule = "user",
            status = "locked"
        ),
        Account(
            id = "acc003",
            fullName = "Lê Văn C",
            userName = "levanc_admin",
            email = "vanc@example.com",
            avatar = "https://example.com/avatar/c.jpg",
            password = "hashed_pw_003",
            rule = "admin",
            status = "active"
        ),
        Account(
            id = "acc004",
            fullName = "Phạm Thị D",
            userName = "phamthid",
            email = "thid@example.com",
            avatar = "https://example.com/avatar/d.jpg",
            password = "hashed_pw_004",
            rule = "user",
            status = "active"
        ),
        Account(
            id = "acc005",
            fullName = "Đỗ Mạnh E",
            userName = "domanhee_admin",
            email = "manhe@example.com",
            avatar = "https://example.com/avatar/e.jpg",
            password = "hashed_pw_005",
            rule = "admin",
            status = "active"
        )
    )

    val getAccounts: LiveData<MutableList<Account>> get() = _setAccounts
    private val _setAccounts = MutableLiveData<MutableList<Account>>()
    fun getAccountList() {
        _setAccounts.value = accounts.toMutableList()
    }
}