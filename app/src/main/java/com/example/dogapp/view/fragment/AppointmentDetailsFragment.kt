package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogapp.databinding.FragmentAppointmentDetailsBinding

class AppointmentDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppointmentDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }
}
