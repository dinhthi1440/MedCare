package com.example.medcare.data.repository.relatives

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.relatives.IRelativesDataSource
import com.example.medcare.models.Account
import com.example.medcare.models.PillReminder
import com.example.medcare.models.Relative
import com.example.medcare.models.Response

class RelativeRepos(private val dataSource: IRelativesDataSource): BaseRepository(), IRelativeRepos {
    override suspend fun getAllRelativesRemote(uid: String): DataResult<Response<Any>> {
        return getResult { dataSource.getAllRelativesRemote(uid) }
    }

    override suspend fun updateRelativesRemote(
        uid: String,
        relative: Relative
    ): DataResult<Response<Any>> {
        return getResult { dataSource.updateRelativesRemote(uid, relative) }
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
        user: Account,
        relative: Relative
    ): DataResult<Response<Any>> {
        return getResult { dataSource.acceptRelativeRequestRemote(user, relative) }
    }

    override suspend fun getAllReminderRelativeFromRemote(uid: String): DataResult<Response<Any>> {
        return getResult { dataSource.getAllReminderRelativeFromRemote(uid) }
    }

    override suspend fun updateReminderRelativeFromToRemote(pillReminder: PillReminder): DataResult<Response<Any>> {
        return getResult { dataSource.updateReminderRelativeFromToRemote(pillReminder) }
    }

    override suspend fun getPillReminderByIdRemote(
        uid: String,
        reminderID: String
    ): DataResult<Response<Any>> {
        return getResult { dataSource.getReminderRelativeFromToByID(uid, reminderID) }
    }

    override suspend fun getRelativeHistoryByRelativeID(
        uid: String,
        relativeID: String
    ): DataResult<Response<Any>> {
        return getResult { dataSource.getRelativeHistoryByRelativeID(uid, relativeID) }
    }

    override suspend fun deleteRelativeReminderRemote(
        uid: String,
        idRelative: String,
        idReminder: String
    ): DataResult<Response<Any>> {
        return getResult {dataSource.deleteRelativeReminderRemote(uid, idRelative, idReminder)}
    }

}