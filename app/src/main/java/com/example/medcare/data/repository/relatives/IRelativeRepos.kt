package com.example.medcare.data.repository.relatives

import com.example.medcare.base.DataResult
import com.example.medcare.models.Account
import com.example.medcare.models.Relative
import com.example.medcare.models.Response

interface IRelativeRepos {
    //relatives
    suspend fun getAllRelativesRemote(uid: String): DataResult<Response<Any>>

    //search
    suspend fun getSearchRelativesRemote(searchString: String): DataResult<Response<Any>>

    //request
    suspend fun insertRelativesRequestRemote(uid: String, relative: Relative): DataResult<Response<Any>>
    suspend fun getAllRelativeRequestRemote(uid: String): DataResult<Response<Any>>
    suspend fun acceptRelativeRequestRemote(user: Account, relative: Relative): DataResult<Response<Any>>

    //reminder
    suspend fun getAllReminderRelativeFromRemote(uid: String): DataResult<Response<Any>>
}