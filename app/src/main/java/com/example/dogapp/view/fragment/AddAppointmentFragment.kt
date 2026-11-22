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
import com.example.dogapp.viewmodel.DogViewModel
import java.util.Locale

class AddAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentAddAppointmentBinding
    private lateinit var dogViewModel: DogViewModel

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
        val updateButtonState = {
            binding.btnSaveAppointment.isEnabled =
                !binding.etPetName.text.isNullOrBlank() &&
                !binding.autoCompleteBreed.text.isNullOrBlank() &&
                !binding.etOwner.text.isNullOrBlank() &&
                !binding.etPhone.text.isNullOrBlank()
        }

        binding.etPetName.doAfterTextChanged { updateButtonState() }
        binding.autoCompleteBreed.doAfterTextChanged { updateButtonState() }
        binding.etOwner.doAfterTextChanged { updateButtonState() }
        binding.etPhone.doAfterTextChanged { updateButtonState() }
        updateButtonState()
    }
}
