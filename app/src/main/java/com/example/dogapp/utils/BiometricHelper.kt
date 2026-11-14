package com.example.dogapp.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dogapp.R

object BiometricHelper {
    // Muestra el diálogo del sistema para autenticar con huella (HU 1.0 criterios 5 y 6)
    fun showFingerprintPrompt(
        fragment: Fragment,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(fragment.requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                // Cancelar cierra el diálogo sin mensaje
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                    errorCode == BiometricPrompt.ERROR_USER_CANCELED
                ) return
                onError(errString.toString())
            }
        }

        val prompt = BiometricPrompt(fragment, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(fragment.getString(R.string.biometric_title))
            .setSubtitle(fragment.getString(R.string.biometric_subtitle))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setNegativeButtonText(fragment.getString(R.string.biometric_cancel))
            .build()

        prompt.authenticate(promptInfo)
    }
}
