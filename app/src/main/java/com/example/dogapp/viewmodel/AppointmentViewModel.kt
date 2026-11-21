package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.model.Appointment
import com.example.dogapp.repository.AppointmentRepository
import com.example.dogapp.repository.DogRepository
import kotlinx.coroutines.launch
import java.util.Locale

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)
    private val dogRepository = DogRepository()

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    val listAppointment: LiveData<List<Appointment>> = appointmentRepository.getListAppointment()

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

    // Eliminar cita con callback para navegar después de completar el borrado
    fun deleteAppointment(appointment: Appointment, onDeleted: () -> Unit) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.deleteAppointment(appointment)
                onDeleted()
            } finally {
                _progresState.value = false
            }
        }
    }

    // Actualiza la cita en Room (HU-05 c8). Si la raza cambió se pide una imagen
    // aleatoria de la nueva raza a la API Dog CEO; si falla se conserva la anterior.
    fun updateAppointment(
        appointment: Appointment,
        previousBreed: String,
        onUpdated: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                var imageUrl = appointment.imageUrl
                if (!appointment.breed.equals(previousBreed, ignoreCase = true)) {
                    try {
                        imageUrl = dogRepository.getBreedImage(appointment.breed.lowercase(Locale.ROOT))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                appointmentRepository.updateAppointment(appointment.copy(imageUrl = imageUrl))
                _progresState.value = false
                onUpdated(true)
            } catch (e: Exception) {
                _progresState.value = false
                onUpdated(false)
            }
        }
    }
}
