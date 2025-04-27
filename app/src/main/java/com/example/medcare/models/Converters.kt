package com.example.medcare.models

import androidx.room.TypeConverter
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSelectedTimeList(value: List<SelectedTime>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSelectedTimeList(value: String): List<SelectedTime> {
        val type = object : TypeToken<List<SelectedTime>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMedicineList(value: List<Medicine>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMedicineList(value: String): List<Medicine> {
        val type = object : TypeToken<List<Medicine>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromFrequencyModel(value: FrequencyModel): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toFrequencyModel(value: String): FrequencyModel {
        return gson.fromJson(value, FrequencyModel::class.java)
    }
}