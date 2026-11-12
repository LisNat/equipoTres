package com.example.dogapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.dogapp.data.AppointmentDB
import com.example.dogapp.data.AppointmentDao
import com.example.dogapp.model.Appointment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepository(val context: Context) {
    private var appointmentDao: AppointmentDao = AppointmentDB.getDatabase(context).appointmentDao()

    suspend fun saveAppointment(appointment: Appointment, messageResponse: (String) -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                appointmentDao.saveAppointment(appointment)
            }
            messageResponse("Cita guardada correctamente")
        } catch (e: Exception) {
            messageResponse("Error al guardar la cita: ${e.message}")
        }
    }

    fun getListAppointment(): LiveData<List<Appointment>> {
        return appointmentDao.getListAppointment()
    }

    fun getAppointmentById(id: Int): LiveData<Appointment?> {
        return appointmentDao.getAppointmentById(id)
    }

    suspend fun deleteAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.deleteAppointment(appointment)
        }
    }

    suspend fun updateAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.updateAppointment(appointment)
        }
    }
}
