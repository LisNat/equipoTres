package com.example.dogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogapp.model.Appointment
import com.example.dogapp.utils.Constants.NAME_BD

@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppointmentDB : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppointmentDB? = null

        fun getDatabase(context: Context): AppointmentDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppointmentDB::class.java,
                    NAME_BD
                ).build().also { INSTANCE = it }
            }
        }
    }
}
