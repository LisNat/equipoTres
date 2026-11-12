package com.example.dogapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dogapp.model.Appointment

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppointment(appointment: Appointment)

    @Query("SELECT * FROM Appointment")
    fun getListAppointment(): LiveData<List<Appointment>>

    @Query("SELECT * FROM Appointment WHERE id = :id")
    fun getAppointmentById(id: Int): LiveData<Appointment?>

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Update
    suspend fun updateAppointment(appointment: Appointment)
}
