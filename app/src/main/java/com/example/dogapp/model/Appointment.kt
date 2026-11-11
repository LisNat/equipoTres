package com.example.dogapp.model

data class Appointment(
    val id: Int = 0,
    val petName: String = "",
    val breed: String = "",
    val ownerName: String = "",
    val phone: String = "",
    val symptom: String = "",
    val imageUrl: String? = null
)
