package com.example.medcare.data.repository.feedback

import com.example.medcare.base.DataResult
import com.example.medcare.models.Feedback
import com.example.medcare.models.Response

interface IFeedbackRepository {
    suspend fun insertFeedback(feedback: Feedback): DataResult<Response<Any>>
    suspend fun updateUserByFiled(accountID: String, fields: Map<String, Any>): DataResult<Response<Any>>
    suspend fun getAllFeedback(): DataResult<Response<Any>>
    suspend fun deleteFeedbackByID(feedbackID: String): DataResult<Response<Any>>
    suspend fun searchFeedback(searchString: String): DataResult<Response<Any>>
    suspend fun getFeedbackByID(feedbackID: String): DataResult<Response<Any>>
}