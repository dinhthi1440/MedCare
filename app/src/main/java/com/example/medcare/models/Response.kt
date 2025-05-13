package com.example.medcare.models

data class Response<T>(
    var statusCode: Int,
    var message: String,
    var data: T? = null
)