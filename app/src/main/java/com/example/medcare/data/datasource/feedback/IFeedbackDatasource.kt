package com.example.medcare.data.datasource.feedback

import com.example.medcare.models.Account
import com.example.medcare.models.Feedback
import com.example.medcare.models.Response

interface IFeedbackDatasource {
    suspend fun insertFeedback(feedback: Feedback): Response<Any>
    suspend fun updateUserByFiled(accountID: String, fields: Map<String, Any>): Response<Any>
    suspend fun getAllFeedback(): Response<Any>
    suspend fun getFeedbackByID(feedbackID: String): Response<Any>
    suspend fun deleteFeedbackByID(feedbackID: String): Response<Any>
    suspend fun searchFeedback(searchString: String): Response<Any>
}