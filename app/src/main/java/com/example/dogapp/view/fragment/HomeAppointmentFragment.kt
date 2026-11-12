package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.dogapp.databinding.FragmentHomeAppointmentBinding

class HomeAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentHomeAppointmentBinding

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
        // No regresa al login, aunque el nav_graph ya quitó Login de la pila (popUpTo).
        // Tenemos q elegir si lo dejamos así o solo que dependa del nav_graph
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)
    }
}
