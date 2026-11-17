package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentEditAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.utils.Constants
import com.example.dogapp.viewmodel.AppointmentViewModel
import com.example.dogapp.viewmodel.DogViewModel
import java.util.Locale

class EditAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentEditAppointmentBinding
    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var dogViewModel: DogViewModel

    private var currentAppointment: Appointment? = null
    private var formFilled = false

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
        appointmentViewModel = ViewModelProvider(this)[AppointmentViewModel::class.java]
        dogViewModel = ViewModelProvider(this)[DogViewModel::class.java]
        // Tras una rotación, los EditText restauran lo que el usuario ya había escrito.
        formFilled = savedInstanceState != null

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        loadAppointment()
        setupBreedSuggestions()
        setupEditButtonState()
        setupEditButton()
        observeProgress()
    }

    private fun loadAppointment() {
        val appointmentId = arguments?.getInt(Constants.ARG_APPOINTMENT_ID, -1) ?: -1
        appointmentViewModel.getAppointmentById(appointmentId)
            .observe(viewLifecycleOwner) { appointment ->
                if (appointment == null) {
                    Toast.makeText(
                        requireContext(), R.string.appointment_not_found, Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigateUp()
                    return@observe
                }
                currentAppointment = appointment
                if (!formFilled) {
                    fillForm(appointment)
                    formFilled = true
                }
                updateEditButtonState()
            }
    }

    private fun fillForm(appointment: Appointment) {
        binding.etPetName.setText(appointment.petName)
        // filter = false evita que se despliegue la lista de sugerencias al precargar.
        binding.autoCompleteBreed.setText(appointment.breed, false)
        binding.etOwner.setText(appointment.ownerName)
        binding.etPhone.setText(appointment.phone)
    }

    private fun setupBreedSuggestions() {
        dogViewModel.listBreed.observe(viewLifecycleOwner) { breeds ->
            val readableBreeds = breeds.map { breed ->
                breed.replaceFirstChar { character ->
                    if (character.isLowerCase()) character.titlecase(Locale.getDefault())
                    else character.toString()
                }
            }
            binding.autoCompleteBreed.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    readableBreeds
                )
            )
        }

        dogViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        if (dogViewModel.listBreed.value.isNullOrEmpty()) {
            dogViewModel.loadListBreed()
        }
    }

    private fun setupEditButtonState() {
        binding.etPetName.doAfterTextChanged { updateEditButtonState() }
        binding.autoCompleteBreed.doAfterTextChanged { updateEditButtonState() }
        binding.etOwner.doAfterTextChanged { updateEditButtonState() }
        binding.etPhone.doAfterTextChanged { updateEditButtonState() }
        updateEditButtonState()
    }

    private fun updateEditButtonState() {
        binding.btnEditAppointment.isEnabled =
            currentAppointment != null &&
            appointmentViewModel.progresState.value != true &&
            !binding.etPetName.text.isNullOrBlank() &&
            !binding.autoCompleteBreed.text.isNullOrBlank() &&
            !binding.etOwner.text.isNullOrBlank() &&
            !binding.etPhone.text.isNullOrBlank()
    }

    private fun setupEditButton() {
        binding.btnEditAppointment.setOnClickListener {
            val appointment = currentAppointment ?: return@setOnClickListener
            appointmentViewModel.editAppointment(
                original = appointment,
                petName = binding.etPetName.text.toString().trim(),
                breed = binding.autoCompleteBreed.text.toString().trim(),
                ownerName = binding.etOwner.text.toString().trim(),
                phone = binding.etPhone.text.toString().trim()
            ) { success ->
                if (!isAdded) return@editAppointment
                if (success) {
                    Toast.makeText(
                        requireContext(), R.string.appointment_updated, Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(
                        R.id.action_editAppointmentFragment_to_homeAppointmentFragment
                    )
                } else {
                    Toast.makeText(
                        requireContext(), R.string.appointment_update_error, Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun observeProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) {
            updateEditButtonState()
        }
    }
}
