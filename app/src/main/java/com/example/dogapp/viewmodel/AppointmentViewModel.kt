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

    /**
     * Actualiza los datos editables de una cita. Si la raza cambió, se consulta
     * una nueva imagen en la Dog API; si la consulta falla se conserva la anterior.
     */
    fun editAppointment(
        original: Appointment,
        petName: String,
        breed: String,
        ownerName: String,
        phone: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                val breedChanged = !original.breed.equals(breed, ignoreCase = true)
                val imageUrl = if (breedChanged) {
                    runCatching {
                        dogRepository.getBreedImage(breed.lowercase(Locale.ROOT))
                    }.getOrNull()
                } else {
                    original.imageUrl
                }
                appointmentRepository.updateAppointment(
                    original.copy(
                        petName = petName,
                        breed = breed,
                        ownerName = ownerName,
                        phone = phone,
                        imageUrl = imageUrl
                    )
                )
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
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
