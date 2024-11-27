package com.example.dadm.view

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.dadm.R
import com.example.dadm.databinding.FragmentLoginBinding
import com.example.dadm.viewmodel.ChallengeViewModel
import com.example.dadm.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController

    private val loginViewModel: LoginViewModel by viewModels()

    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupEmailValidation()
        setupPasswordValidation()
        setupPasswordVisibilityToggle()

        setupRegisterButtonAnimation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Configuración de observadores y estados
        setupButtonStates()

        // Listener para el botón de login
        binding.loginButton.setOnClickListener {
            // Implementa aquí la lógica para realizar el login
        }

        // Listener para el botón de registro
        binding.registerButton.setOnClickListener {
            // Implementa la acción de registro aquí
        }
    }

    private fun setupEmailValidation() {
        // Observa la visibilidad del error del email
        loginViewModel.emailErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvEmailError.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }


    private fun setupPasswordValidation() {
        // Observa la visibilidad del error de la contraseña
        loginViewModel.passwordErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvPasswordError.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    // Configuración del cambio de visibilidad de la contraseña
    private fun setupPasswordVisibilityToggle() {
        // Detectar clics en el ícono del "ojo" para alternar la visibilidad de la contraseña
        binding.ilPassword.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible // Cambiar el estado de visibilidad

            if (isPasswordVisible) {
                // Mostrar la contraseña
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.ilPassword.setEndIconDrawable(R.drawable.ic_eye_open) // Cambiar el icono a cerrado
            } else {
                // Ocultar la contraseña
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.ilPassword.setEndIconDrawable(R.drawable.ic_eye_closed) // Cambiar el icono a abierto
            }

            // Actualizar el campo de texto para reflejar el cambio en la visibilidad de la contraseña
            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
        }
    }

    private fun setupButtonStates() {
        // Observamos el estado del botón de login
        loginViewModel.enableLoginButton.observe(viewLifecycleOwner) { isEnabled ->
            binding.loginButton.isEnabled = isEnabled
            // Cambiar dinámicamente el color del fondo y el texto del botón de login

            binding.loginButton.setTextColor(
                if (isEnabled) resources.getColor(R.color.white)
                else resources.getColor(R.color.app_red)
            )
        }

        // Observamos el estado del botón de registro
        loginViewModel.enableRegisterButton.observe(viewLifecycleOwner) { isEnabled ->
            binding.registerButton.isEnabled = isEnabled
            // Cambiar dinámicamente el color del texto del botón de registro
            binding.registerButton.setTextColor(
                if (isEnabled) resources.getColor(R.color.white)
                else resources.getColor(R.color.app_gray)
            )
        }
    }

    private fun setupRegisterButtonAnimation() {
        binding.registerButton.setOnClickListener {
            // Aquí aplicamos la animación al TextView cuando se hace clic
            val scaleAnimation = ScaleAnimation(
                1f, 0.95f, // De tamaño original a más pequeño (reducción)
                1f, 0.95f, // De tamaño original a más pequeño (reducción)
                Animation.RELATIVE_TO_SELF, 0.5f, // Punto de pivote en el centro horizontal
                Animation.RELATIVE_TO_SELF, 0.5f // Punto de pivote en el centro vertical
            ).apply {
                duration = 150 // Duración de la animación en milisegundos
                repeatCount = 1 // Hacer que la animación se repita una vez
                repeatMode = Animation.REVERSE // Revertir la animación después de completar
            }

            // Iniciar la animación
            binding.registerButton.startAnimation(scaleAnimation)


        }
    }


}
