package com.example.medcare.data.repository.relatives

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.relatives.IRelativesDataSource
import com.example.medcare.models.Relative
import com.example.medcare.models.Response

class RelativeRepos(private val dataSource: IRelativesDataSource): BaseRepository(), IRelativeRepos {
    override suspend fun getAllRelativesRemote(uid: String): DataResult<Response<Any>> {
        return getResult { dataSource.getAllRelativesRemote(uid) }
    }

    override suspend fun getSearchRelativesRemote(searchString: String): DataResult<Response<Any>> {
        return getResult { dataSource.getSearchRelativesRemote(searchString) }
    }

    override suspend fun insertRelativesRequestRemote(
        uid: String,
        relative: Relative
    ): DataResult<Response<Any>> {
        return getResult { dataSource.insertRelativesRequestRemote(uid, relative) }
    }

    override suspend fun getAllRelativeRequestRemote(uid: String): DataResult<Response<Any>> {
        return getResult { dataSource.getAllRelativeRequestRemote(uid) }
    }

    override suspend fun acceptRelativeRequestRemote(
        uid: String,
        relative: Relative
    ): DataResult<Response<Any>> {
        return getResult { dataSource.acceptRelativeRequestRemote(uid, relative) }
    }

}