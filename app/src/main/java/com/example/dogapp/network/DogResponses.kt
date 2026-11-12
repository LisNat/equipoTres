package com.example.dogapp.network

// Respuesta de breeds/list/all: {"message": {"akita": [], "hound": ["afghan"]}, "status": "success"}
data class DogBreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

// Respuesta de breed/{raza}/images/random: {"message": "https://...jpg", "status": "success"}
data class DogImageResponse(
    val message: String,
    val status: String
)
