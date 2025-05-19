package com.example.medcare.data.repository.relatives

import com.example.medcare.base.DataResult
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
    suspend fun acceptRelativeRequestRemote(uid: String, relative: Relative): DataResult<Response<Any>>
}