package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentHomeAppointmentBinding
import com.example.dogapp.model.Appointment
import com.example.dogapp.view.adapter.AppointmentAdapter
import com.example.dogapp.viewmodel.AppointmentViewModel

class HomeAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentHomeAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // HU 2.0 criterio 1: el botón atrás de Home cierra la app y lleva al escritorio del celular.
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

        controladores()
        observadorViewModel()
    }

    // Configura los listeners de los controles (HU-02 c4)
    private fun controladores() {
        // FAB para agregar una nueva cita (HU-02 c4)
        binding.fbAgregar.setOnClickListener {
            findNavController().navigate(R.id.action_homeAppointmentFragment_to_addAppointmentFragment)
        }
    }

    // Observa los LiveData del ViewModel (HU-02 c5, c6)
    private fun observadorViewModel() {
        // Observar la lista de citas desde la base de datos Room (HU-02 c6)
        appointmentViewModel.listAppointment.observe(viewLifecycleOwner) { listaCitas ->
            val mutableList = listaCitas.toMutableList()
            binding.rvCitas.layoutManager = LinearLayoutManager(context)
            binding.rvCitas.adapter = AppointmentAdapter(mutableList, findNavController())
        }

        // Observar el estado del progreso
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}
