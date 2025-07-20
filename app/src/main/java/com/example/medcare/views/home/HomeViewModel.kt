package com.example.medcare.views.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.home.IHomeRepository
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.models.PillReminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val iHomeRepository: IHomeRepository) : BaseViewModel(){

    fun dataSynchronization(uid: String, context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val alarmHelper = AlarmHelper(context)
                val localHistory = iHomeRepository.getAllHistory()
                if (localHistory.isNotEmpty()) {
                    val uploadResult = iHomeRepository.insertHistoryRemote(uid, localHistory)
                    if (uploadResult.statusCode == 200) {
                        iHomeRepository.deleteAllHistory()
                    }
                }

                val remoteMedicineResult = iHomeRepository.getAllMedicineRemote(uid)
                val remoteReminderResult = iHomeRepository.getAllReminderRemote(uid)

                if (remoteMedicineResult.statusCode == 200 && remoteReminderResult.statusCode == 200) {
                    val medicineList = remoteMedicineResult.data as List<Medicine>
                    val reminderList = remoteReminderResult.data as List<PillReminder>
                    val reminder = iHomeRepository.getAllPillReminder()
                    reminder.forEach { reminder ->
                        alarmHelper.removeAlarmByReminder(context, reminder)
                    }
                    iHomeRepository.deleteAllMedicine()
                    iHomeRepository.insertAllMedicine(medicineList)
                    reminder.forEach { reminder ->
                        alarmHelper.registerAlarm(context, reminder)
                    }
                    iHomeRepository.deleteAllReminder()
                    iHomeRepository.insertAllReminder(reminderList)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}