package com.example.dogapp.repository

import com.example.dogapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {
    private val dogApiService = RetrofitClient.api

    suspend fun getListBreed(): List<String> {
        return withContext(Dispatchers.IO) {
            dogApiService.getListBreed().message.keys.sorted()
        }
    }

    suspend fun getBreedImage(breed: String): String {
        return withContext(Dispatchers.IO) {
            dogApiService.getBreedImage(breed).message
        }
    }
}
