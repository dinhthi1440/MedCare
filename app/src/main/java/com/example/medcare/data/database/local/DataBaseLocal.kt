package com.example.medcare.data.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medcare.data.database.local.dao.MedicineDAO
import com.example.medcare.data.database.local.dao.PillReminderDAO
import com.example.medcare.models.Converters
import com.example.medcare.models.Medicine
import com.example.medcare.views.pill_reminder.model.PillReminder

@Database(
    entities = [Medicine::class, PillReminder::class],
    version = DataBaseLocal.VERSION
)
@TypeConverters(Converters::class)
abstract class DataBaseLocal : RoomDatabase() {

    abstract val medicineDao: MedicineDAO
    abstract val pillReminderDAO: PillReminderDAO
    companion object {
        const val NAME = "MedCare"
        const val VERSION = 1
        const val TABLE_MEDICINE = "Medicine"
        const val TABLE_PILL_REMINDER = "PillReminder"
    }
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: DataBaseLocal? = null

    fun getDatabase(context: Context): DataBaseLocal {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                DataBaseLocal::class.java,
                DataBaseLocal.NAME
            ).build()
            INSTANCE = instance
            instance
        }
    }
}