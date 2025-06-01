package com.example.medcare.extension

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.DateCustom
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.views.main.AlarmReceiver // Đảm bảo đúng package path
import com.example.medcare.models.PillReminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.Calendar

class AlarmHelper(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private val ttsHelper = TTSHelper(context) // Đảm bảo TTSHelper tồn tại và hoạt động tốt

    fun startAlarm() {
        ttsHelper.speak("Đã đến giờ uống thuốc!")
        val alarmUri = Settings.System.DEFAULT_RINGTONE_URI
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, alarmUri)
            isLooping = true
            prepare()
            start()
        }

        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 500, 500)
        val effect = VibrationEffect.createWaveform(pattern, 0)
        vibrator?.vibrate(effect)
    }

    fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        ttsHelper.shutdown()
        vibrator?.cancel()
    }
    fun registerAlarm(context: Context, reminder: PillReminder) {
        val today = Calendar.getInstance()

        reminder.times.forEach { timeSelected ->
            val (hourStr, minuteStr) = timeSelected.time.split(":")
            val hour = hourStr.toInt()
            val minute = minuteStr.toInt()

            val baseCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val frequency = reminder.frequency
            val label = frequency.label
            val selectedDays = frequency.listDateSelected

            when (label) {
                "Hôm nay" -> {
                    // Chỉ đặt nếu thời gian báo thức chưa qua trong hôm nay
                    if (baseCalendar.after(today)) {
                        scheduleSingleAlarm(context, reminder, timeSelected, baseCalendar)
                    }
                }

                "Mỗi ngày" -> {
                    val nextAlarmCalendar = if (baseCalendar.before(today)) {
                        baseCalendar.apply { add(Calendar.DAY_OF_MONTH, 1) }
                    } else {
                        baseCalendar
                    }
                    scheduleRepeatingAlarm(context, reminder, timeSelected, nextAlarmCalendar, "daily")
                }

                "Cách ngày" -> {
                    val nextAlarmCalendar = if (baseCalendar.before(today)) {
                        baseCalendar.apply { add(Calendar.DAY_OF_MONTH, 2) } // Lên lịch cho 2 ngày sau
                    } else {
                        baseCalendar
                    }
                    scheduleRepeatingAlarm(context, reminder, timeSelected, nextAlarmCalendar, "every_other_day")
                }

                "Tuỳ chỉnh" -> {
                    if (!selectedDays.isNullOrEmpty()) {
                        // Tìm ngày trong tuần gần nhất phù hợp
                        for (i in 0..7) { // Kiểm tra 7 ngày tới để tìm ngày đầu tiên hợp lệ
                            val checkCalendar = baseCalendar.clone() as Calendar
                            checkCalendar.add(Calendar.DAY_OF_YEAR, i)

                            val dayOfWeek = checkCalendar.get(Calendar.DAY_OF_WEEK)
                            val dateCustom = dayOfWeekToDateCustom(dayOfWeek)

                            // Nếu là ngày đã chọn VÀ thời gian chưa qua trong ngày đó
                            if (selectedDays.contains(dateCustom) && checkCalendar.after(today)) {
                                scheduleRepeatingAlarm(context, reminder, timeSelected, checkCalendar, "custom")
                                break // Chỉ đặt cho lần đầu tiên, các lần sau sẽ được đặt lại trong Receiver
                            }
                        }
                    }
                }

                else -> { // Default case, could be similar to "Mỗi ngày" or just a single alarm
                    val nextAlarmCalendar = if (baseCalendar.before(today)) {
                        baseCalendar.apply { add(Calendar.DAY_OF_MONTH, 1) }
                    } else {
                        baseCalendar
                    }
                    scheduleSingleAlarm(context, reminder, timeSelected, nextAlarmCalendar)
                }
            }
        }
    }

    private fun dayOfWeekToDateCustom(dayOfWeek: Int): DateCustom = when (dayOfWeek) {
        Calendar.MONDAY -> DateCustom.Monday
        Calendar.TUESDAY -> DateCustom.Tuesday
        Calendar.WEDNESDAY -> DateCustom.Wednesday
        Calendar.THURSDAY -> DateCustom.Thursday
        Calendar.FRIDAY -> DateCustom.Friday
        Calendar.SATURDAY -> DateCustom.Saturday
        Calendar.SUNDAY -> DateCustom.Sunday
        else -> DateCustom.Monday
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleSingleAlarm(
        context: Context,
        reminder: PillReminder,
        timeSelected: SelectedTime,
        calendar: Calendar
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", timeSelected.id.toString())
            putExtra("reminder_id", reminder.id)
            putExtra("alarm_message", "${reminder.label}: Đến giờ uống thuốc!")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            timeSelected.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                println("AlarmHelper: Exact alarm permission not granted. Alarm might not fire precisely.")
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    // Phương thức riêng cho báo thức lặp lại (cần lên lịch lại trong Receiver)
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleRepeatingAlarm(
        context: Context,
        reminder: PillReminder,
        timeSelected: SelectedTime,
        calendar: Calendar,
        repeatType: String // "daily", "every_other_day", "custom"
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", timeSelected.id.toString())
            putExtra("reminder_id", reminder.id)
            putExtra("alarm_message", "${reminder.label}: Đến giờ uống thuốc!")
            putExtra("repeat_type", repeatType) // Truyền loại lặp lại để Receiver biết cách lên lịch lại
            putExtra("selected_days", if (repeatType == "custom") reminder.frequency.listDateSelected?.map { it.name }?.toTypedArray() else null) // Chỉ gửi cho custom
            putExtra("pill_reminder_object", reminder) // Truyền cả đối tượng PillReminder nếu cần nhiều thông tin hơn trong Receiver
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            timeSelected.id, // Request code là ID của SelectedTime
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                println("AlarmHelper: Exact alarm permission not granted. Alarm might not fire precisely for repeating schedule.")
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun removeAlarm(context: Context, requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }
    fun removeAlarmByReminder(context: Context, pillReminder: PillReminder){
        pillReminder.times.forEach { time ->
            removeAlarm(context, time.id)
        }
    }
}