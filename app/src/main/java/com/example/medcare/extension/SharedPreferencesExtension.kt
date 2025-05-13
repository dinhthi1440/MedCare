package com.example.medcare.extension

import android.content.SharedPreferences
import com.example.medcare.utils.Constants

fun SharedPreferences.saveUserID(userID: String){
    this.edit().putString(Constants.SHARED_USER_ID, userID).apply()
}

fun SharedPreferences.getUserID(): String?{
    return this.getString(Constants.SHARED_USER_ID, Constants.SHARED_DEFAULT)
}