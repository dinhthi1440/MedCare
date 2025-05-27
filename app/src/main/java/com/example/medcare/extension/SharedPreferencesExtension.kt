package com.example.medcare.extension

import android.content.SharedPreferences
import com.example.medcare.utils.Constants
import androidx.core.content.edit

fun SharedPreferences.saveData(dataString: String, constKey: String){
    this.edit { putString(constKey, dataString) }
}

fun SharedPreferences.getData(constKey: String): String{
    return this.getString(constKey, Constants.SHARED_DEFAULT) ?: ""
}

fun SharedPreferences.saveDataBoolean(value: Boolean, key: String) {
    edit { putBoolean(key, value) }
}

fun SharedPreferences.getDataBoolean(key: String): Boolean {
    return getBoolean(key, false)
}