package com.example.medcare.views.main

import android.app.KeyguardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.data.database.local.DatabaseProvider
import com.example.medcare.databinding.ActivityAlertBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.extension.getData
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderHistory
import com.example.medcare.utils.Constants
import com.example.medcare.utils.TimeUtils
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import java.util.UUID
import kotlin.String

class AlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlertBinding
    private val medicineAdapter by lazy { MedicineAdapter(false, null) }
    private lateinit var alarmHelper: AlarmHelper
    private lateinit var database: DataBaseLocal
    private lateinit var reminderData: PillReminder
    private val sharedPreferences by lazy { get<SharedPreferences>() }
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)

            val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            if (keyguardManager.isKeyguardLocked) {
                keyguardManager.requestDismissKeyguard(this, null)
            }
        }
        alarmHelper = AlarmHelper(this)
        val uid = sharedPreferences.getData(Constants.SHARED_USER_ID)
        database = DatabaseProvider.getDatabase(this)
        val reminderId = intent?.getStringExtra("reminder_id") ?: ""
        val alarmID = intent?.getStringExtra("alarm_id") ?: "0"
        reminderId.let { id ->
            CoroutineScope(Dispatchers.Main).launch {
                val reminder = withContext(Dispatchers.IO) {
                    database.pillReminderDAO.getPillReminderById(id)
                }
                if (reminder?.isOn == true && reminder.receiverID == uid) {
                    //alarmHelper.startAlarm()
                    reminder.let { pillReminder ->
                        reminderData = pillReminder
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
            CoroutineScope(Dispatchers.IO).launch  {
                alarmHelper.stopAlarm()
                val history = ReminderHistory(
                    UUID.randomUUID().toString(),
                    reminderData.label,
                    TimeUtils.getCurrentDate(),
                    TimeUtils.getCurrentTime(),
                    reminderData.note,
                    reminderData
                )
                database.historyDAO.insertHistory(history)
                uid.let {
                    val docRef = db.collection("users")
                        .document(it)
                        .collection("reminder_history")
                        .document(history.id)
                    docRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (!documentSnapshot.exists()) {
                                docRef.set(history)
                            }
                        }
                }

            }

        }

        binding.btnOk.setOnClickListener {
//            alarmHelper.stopAlarm()
//            finish()
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