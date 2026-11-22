package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentAddAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.viewmodel.AppointmentViewModel
import com.example.dogapp.viewmodel.DogViewModel
import java.util.Locale

class AddAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentAddAppointmentBinding
    private lateinit var dogViewModel: DogViewModel
    private lateinit var appointmentViewModel: AppointmentViewModel
    private var isSavingAppointment = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dogViewModel = ViewModelProvider(this)[DogViewModel::class.java]
        appointmentViewModel = ViewModelProvider(this)[AppointmentViewModel::class.java]

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.spinnerSymptoms.setSelection(0)
        setupBreedSuggestions()
        setupSaveButtonState()
        binding.btnSaveAppointment.setOnClickListener {
            if (binding.spinnerSymptoms.selectedItemPosition == 0) {
                Toast.makeText(
                    requireContext(),
                    R.string.select_symptom_validation,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            saveAppointment()
        }
    }

    private fun saveAppointment() {
        val appointment = Appointment(
            petName = binding.etPetName.text.toString().trim(),
            breed = binding.autoCompleteBreed.text.toString().trim(),
            ownerName = binding.etOwner.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim(),
            symptom = binding.spinnerSymptoms.selectedItem.toString()
        )
        isSavingAppointment = true
        updateSaveButtonState()

        appointmentViewModel.saveAppointment(appointment) { result ->
            if (!isAdded ||
                findNavController().currentDestination?.id != R.id.addAppointmentFragment
            ) {
                return@saveAppointment
            }

            result.onSuccess { imageLoaded ->
                if (!imageLoaded) {
                    Toast.makeText(
                        requireContext(),
                        R.string.appointment_saved_without_image,
                        Toast.LENGTH_LONG
                    ).show()
                }
                findNavController().popBackStack(R.id.homeAppointmentFragment, false)
            }.onFailure {
                isSavingAppointment = false
                updateSaveButtonState()
                Toast.makeText(
                    requireContext(),
                    R.string.appointment_save_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
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

    private fun setupSaveButtonState() {
        binding.etPetName.doAfterTextChanged { updateSaveButtonState() }
        binding.autoCompleteBreed.doAfterTextChanged { updateSaveButtonState() }
        binding.etOwner.doAfterTextChanged { updateSaveButtonState() }
        binding.etPhone.doAfterTextChanged { updateSaveButtonState() }
        updateSaveButtonState()
    }

    private fun updateSaveButtonState() {
        binding.btnSaveAppointment.isEnabled =
            !isSavingAppointment &&
            !binding.etPetName.text.isNullOrBlank() &&
            !binding.autoCompleteBreed.text.isNullOrBlank() &&
            !binding.etOwner.text.isNullOrBlank() &&
            !binding.etPhone.text.isNullOrBlank()
    }
}
