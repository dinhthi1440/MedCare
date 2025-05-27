package com.example.medcare.data.repository.feedback

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.auth.IAuthDatasource
import com.example.medcare.data.datasource.feedback.IFeedbackDatasource
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.models.Feedback
import com.example.medcare.models.Response

class FeedbackRepository (private val remote: IFeedbackDatasource) : BaseRepository(), IFeedbackRepository {
    override suspend fun insertFeedback(feedback: Feedback): DataResult<Response<Any>> {
        return getResult { remote.insertFeedback(feedback) }
    }

    override suspend fun updateUserByFiled(
        accountID: String,
        fields: Map<String, Any>
    ): DataResult<Response<Any>> {
        return getResult { remote.updateUserByFiled(accountID, fields) }
    }

    override suspend fun getAllFeedback(): DataResult<Response<Any>> {
        return getResult { remote.getAllFeedback() }
    }

    override suspend fun deleteFeedbackByID(feedbackID: String): DataResult<Response<Any>> {
        return getResult { remote.deleteFeedbackByID(feedbackID) }
    }

    override suspend fun searchFeedback(searchString: String): DataResult<Response<Any>> {
        return getResult { remote.searchFeedback(searchString) }
    }

    override suspend fun getFeedbackByID(feedbackID: String): DataResult<Response<Any>> {
        return getResult { remote.getFeedbackByID(feedbackID) }
    }

}