package com.example.dogapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dogapp.databinding.ItemCitaBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.view.viewholder.AppointmentViewHolder

// Adapter para el RecyclerView de citas (HU-02 c5, c6)
class AppointmentAdapter(
    private val listaCitas: MutableList<Appointment>,
    private val navController: NavController
) : RecyclerView.Adapter<AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemCitaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding, navController)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.setItemCita(listaCitas[position])
    }

    override fun getItemCount(): Int = listaCitas.size
}
