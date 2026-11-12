package com.example.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.databinding.FragmentLoginBinding
import com.example.dogapp.utils.BiometricHelper

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animationFingerprint.setOnClickListener {
            BiometricHelper.showFingerprintPrompt(
                fragment = this,
                onSuccess = {
                    // HU 1.0 criterio 6: huella correcta, entra a Home (popUpTo quita Login)
                    findNavController().navigate(R.id.action_loginFragment_to_homeAppointmentFragment)
                },
                onError = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
