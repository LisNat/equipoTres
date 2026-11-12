package com.example.dogapp.network

import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {
    @GET("breeds/list/all")
    suspend fun getListBreed(): DogBreedsResponse

    @GET("breed/{breed}/images/random")
    suspend fun getBreedImage(@Path("breed") breed: String): DogImageResponse
}
