package com.example.medcare.extension
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.DateCustom
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.views.main.AlarmReceiver
import com.example.medcare.models.PillReminder
import java.util.Calendar

class AlarmHelper(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private val ttsHelper = TTSHelper(context)

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
    @SuppressLint("ScheduleExactAlarm")
    fun registerAlarm(context: Context, reminder: PillReminder) {
        val today = Calendar.getInstance()

        reminder.times.forEach { timeSelected ->
            val (hourStr, minuteStr) = timeSelected.time.split(":")
            var hour = hourStr.toInt()
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
                    if (baseCalendar.after(today)) {
                        scheduleAlarm(context, reminder, timeSelected, baseCalendar)
                    }
                }

                "Mỗi ngày" -> {
                    if (baseCalendar.before(today)) {
                        baseCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    }
                    scheduleAlarm(context, reminder, timeSelected, baseCalendar)
                }

                "Cách ngày" -> {
                    // Đặt alarm cho hôm nay hoặc ngày kế tiếp cách 2 ngày
                    val startCalendar = if (baseCalendar.before(today)) {
                        baseCalendar.add(Calendar.DAY_OF_MONTH, 2)
                        baseCalendar
                    } else {
                        baseCalendar
                    }
                    scheduleAlarm(context, reminder, timeSelected, startCalendar)
                }

                "Tuỳ chỉnh" -> {
                    if (!selectedDays.isNullOrEmpty()) {
                        // Tìm ngày trong tuần gần nhất phù hợp
                        for (i in 0..6) {
                            val checkCalendar = baseCalendar.clone() as Calendar
                            checkCalendar.add(Calendar.DAY_OF_YEAR, i)

                            val dayOfWeek = checkCalendar.get(Calendar.DAY_OF_WEEK)
                            val dateCustom = dayOfWeekToDateCustom(dayOfWeek)

                            if (selectedDays.contains(dateCustom)) {
                                if (checkCalendar.after(today)) {
                                    scheduleAlarm(context, reminder, timeSelected, checkCalendar)
                                    break
                                }
                            }
                        }
                    }
                }

                else -> {
                    if (baseCalendar.before(today)) {
                        baseCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    }
                    scheduleAlarm(context, reminder, timeSelected, baseCalendar)
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
    private fun scheduleAlarm(
        context: Context,
        reminder: PillReminder,
        timeSelected: SelectedTime,
        calendar: Calendar
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("alarm_id", timeSelected.id)
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
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }

    fun removeAlarm(context: Context, requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}