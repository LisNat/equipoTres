package com.example.dogapp.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentAppointmentDetailsBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.viewmodel.AppointmentViewModel

class AppointmentDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentDetailsBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private lateinit var receivedAppointment: Appointment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataCita()
        controladores()
    }

    // Recibe y muestra los datos de la cita seleccionada (HU-04 c1, c3)
    private fun dataCita() {
        val appointmentArg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("clave", Appointment::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("clave") as? Appointment
        }

        receivedAppointment = appointmentArg ?: Appointment()

        mostrarDatos(receivedAppointment)
    }

    private fun mostrarDatos(appointment: Appointment) {
        // Título de la toolbar con el nombre de la mascota (HU-04 c1)
        binding.tvTituloToolbar.text = appointment.petName

        // Turno (ID autogenerado) en la parte superior derecha (HU-04 c3)
        binding.tvTurno.text = "#${appointment.id}"

        // Raza en negro bold (HU-04 c3)
        binding.tvRaza.text = appointment.breed

        // Síntoma (HU-04 c3)
        binding.tvSintoma.text = appointment.symptom

        // Propietario (HU-04 c3)
        binding.tvPropietario.text = "Propietario: ${appointment.ownerName}"

        // Teléfono (HU-04 c3)
        binding.tvTelefono.text = "Teléfono: ${appointment.phone}"

        // Cargar imagen de la mascota usando Coil (HU-04 c2)
        cargarImagen(appointment.imageUrl)
    }

    // Configura los listeners de los controles (HU-04 c1, c4, c5)
    private fun controladores() {
        // Flecha de atrás en la toolbar navega al Home (HU-04 c1)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // FAB Eliminar: borra la cita y navega al Home (HU-04 c4)
        binding.fbDelete.setOnClickListener {
            appointmentViewModel.deleteAppointment(receivedAppointment) {
                findNavController().popBackStack()
            }
        }

        // FAB Editar: navega a la pantalla de editar cita con los datos (HU-04 c5)
        binding.fbEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataAppointment", receivedAppointment)
            findNavController().navigate(
                R.id.action_appointmentDetailsFragment_to_editAppointmentFragment,
                bundle
            )
        }
    }

    /**
     * Carga la imagen de la mascota desde una URL usando Coil (HU-04 c2).
     */
    private fun cargarImagen(url: String?) {
        if (!url.isNullOrBlank()) {
            binding.ivMascota.load(url) {
                crossfade(true)
                placeholder(R.drawable.ic_dog_placeholder)
                error(R.drawable.ic_dog_placeholder)
            }
        } else {
            binding.ivMascota.setImageResource(R.drawable.ic_dog_placeholder)
        }
    }
}
