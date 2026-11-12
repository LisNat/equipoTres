package com.example.dogapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogapp.repository.DogRepository
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {
    private val dogRepository = DogRepository()

    private val _listBreed = MutableLiveData<List<String>>(emptyList())
    val listBreed: LiveData<List<String>> = _listBreed

    private val _breedImage = MutableLiveData<String?>()
    val breedImage: LiveData<String?> = _breedImage

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadListBreed() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listBreed.value = dogRepository.getListBreed()
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la lista de razas"
            } finally {
                _progresState.value = false
            }
        }
    }

    fun loadBreedImage(breed: String) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _breedImage.value = dogRepository.getBreedImage(breed)
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la imagen de la raza"
            } finally {
                _progresState.value = false
            }
        }
    }
}
