package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.model.Appointment
import com.example.dogapp.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    val listAppointment: LiveData<List<Appointment>> = appointmentRepository.getListAppointment()

    fun getAppointmentById(id: Int): LiveData<Appointment?> {
        return appointmentRepository.getAppointmentById(id)
    }

    fun saveAppointment(appointment: Appointment, message: (String) -> Unit) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.saveAppointment(appointment) { msg ->
                    message(msg)
                }
            } finally {
                _progresState.value = false
            }
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.deleteAppointment(appointment)
            } finally {
                _progresState.value = false
            }
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.updateAppointment(appointment)
            } finally {
                _progresState.value = false
            }
        }
    }
}
