package com.example.dogapp.view.viewholder

import androidx.navigation.NavController
import android.os.Bundle
import com.example.dogapp.R
import com.example.dogapp.databinding.ItemCitaBinding
import com.example.dogapp.model.Appointment
import androidx.recyclerview.widget.RecyclerView

// ViewHolder para cada ítem de la lista de citas (HU-02 c5)
class AppointmentViewHolder(
    private val binding: ItemCitaBinding,
    private val navController: NavController
) : RecyclerView.ViewHolder(binding.root) {

    // Asigna los datos de la cita al ítem
    fun setItemCita(appointment: Appointment) {
        binding.tvNombreMascota.text = appointment.petName
        binding.tvSintoma.text = appointment.symptom
        binding.tvTurno.text = "#${appointment.id}"

        // Al hacer clic en el CardView navega al detalle de la cita (HU-02 c5)
        binding.cvCita.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", appointment)
            navController.navigate(
                R.id.action_homeAppointmentFragment_to_appointmentDetailsFragment,
                bundle
            )
        }
    }
}
