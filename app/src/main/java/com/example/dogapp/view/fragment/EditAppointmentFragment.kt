package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentEditAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.viewmodel.AppointmentViewModel
import com.example.dogapp.viewmodel.DogViewModel
import java.util.Locale

class EditAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentEditAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private val dogViewModel: DogViewModel by viewModels()
    private lateinit var receivedAppointment: Appointment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // controladores() va primero para que validarDatos() habilite el botón
        // al precargar los campos con los datos de la cita
        controladores()
        dataAppointment()
        observadorViewModel()
    }

    // Recibe la cita desde el detalle y precarga los campos (HU-05 c2, c3, c4, c5)
    private fun dataAppointment() {
        val receivedBundle = arguments
        receivedAppointment = receivedBundle?.getSerializable("dataAppointment") as Appointment
        binding.etPetName.setText(receivedAppointment.petName)
        // filter = false evita que se despliegue la lista de razas al precargar
        binding.autoCompleteBreed.setText(receivedAppointment.breed, false)
        binding.etOwner.setText(receivedAppointment.ownerName)
        binding.etPhone.setText(receivedAppointment.phone)
    }

    // Configura los listeners de los controles (HU-05 c1, c8)
    private fun controladores() {
        validarDatos()

        // Flecha atrás regresa al detalle de la cita (HU-05 c1)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Botón Editar Cita actualiza en Room y navega al Home (HU-05 c8)
        binding.btnEditAppointment.setOnClickListener {
            updateAppointment()
        }
    }

    // El botón solo se habilita si todos los campos están llenos (HU-05 c6, c7)
    private fun validarDatos() {
        val listEditText = listOf(
            binding.etPetName,
            binding.autoCompleteBreed,
            binding.etOwner,
            binding.etPhone
        )

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all {
                    it.text.toString().isNotBlank()
                }
                binding.btnEditAppointment.isEnabled = isListFull
            }
        }
    }

    private fun updateAppointment() {
        val petName = binding.etPetName.text.toString().trim()
        val breed = binding.autoCompleteBreed.text.toString().trim()
        val ownerName = binding.etOwner.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val appointment = receivedAppointment.copy(
            petName = petName,
            breed = breed,
            ownerName = ownerName,
            phone = phone
        )

        binding.btnEditAppointment.isEnabled = false
        appointmentViewModel.updateAppointment(appointment, receivedAppointment.breed) { isUpdated ->
            if (!isAdded) return@updateAppointment
            if (isUpdated) {
                Toast.makeText(context, R.string.appointment_updated, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editAppointmentFragment_to_homeAppointmentFragment)
            } else {
                Toast.makeText(context, R.string.appointment_update_error, Toast.LENGTH_LONG).show()
                binding.btnEditAppointment.isEnabled = true
            }
        }
    }

    // Observa los LiveData de los ViewModel (HU-05 c3)
    private fun observadorViewModel() {
        observerListBreed()

        dogViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Lista de razas desde la API Dog CEO para el AutoCompleteTextView (HU-05 c3)
    private fun observerListBreed() {
        dogViewModel.loadListBreed()
        dogViewModel.listBreed.observe(viewLifecycleOwner) { lista ->
            val listaRazas = lista.map { raza ->
                raza.replaceFirstChar { it.titlecase(Locale.getDefault()) }
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listaRazas
            )
            binding.autoCompleteBreed.setAdapter(adapter)
        }
    }
}
