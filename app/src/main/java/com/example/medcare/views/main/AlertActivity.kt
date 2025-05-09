package com.example.medcare.views.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.data.database.local.DatabaseProvider
import com.example.medcare.databinding.ActivityAlertBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.views.pill_reminder.model.PillReminder
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlertBinding
    private val medicineAdapter by lazy { MedicineAdapter(false, null) }
    private lateinit var alarmHelper: AlarmHelper
    private lateinit var database: DataBaseLocal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmHelper = AlarmHelper(this)
        database = DatabaseProvider.getDatabase(this)
        val reminderId = intent?.getStringExtra("reminder_id")
        val alarmID = intent?.getStringExtra("alarm_id") ?: "0"
        reminderId?.let { id ->
            CoroutineScope(Dispatchers.Main).launch {
                val reminder = withContext(Dispatchers.IO) {
                    database.pillReminderDAO.getPillReminderById(id)
                }
                if (reminder?.isOn == true) {
                    alarmHelper.startAlarm()
                    reminder.let { pillReminder ->
                        val timeObject = pillReminder.times.find { it.id == alarmID.toInt() }
                        if (timeObject != null) {
                            setupUI(pillReminder, timeObject)
                        }
                    }
                } else {
                    finish()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            alarmHelper.stopAlarm()
            finish()
        }

        binding.btnOk.setOnClickListener {
            alarmHelper.stopAlarm()
            finish()
        }
    }

    private fun setupUI(pillReminder: PillReminder, timeObject: SelectedTime) {
        binding.txtLabel.text = pillReminder.label
        binding.txtTime.text = timeObject.time

        binding.rcvMedicineList.layoutManager = LinearLayoutManager(this)
        binding.rcvMedicineList.adapter = medicineAdapter
        medicineAdapter.submitList(pillReminder.medicines)

        if (pillReminder.medicines.size > 2) {
            binding.txtSeeMore.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmHelper.stopAlarm()
    }
}