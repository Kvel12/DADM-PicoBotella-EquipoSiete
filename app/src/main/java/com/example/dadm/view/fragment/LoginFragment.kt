package com.example.dadm.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        setupPasswordValidation()
        setupPasswordVisibilityToggle()

        return binding.root
    }

    // Configuración de la validación en tiempo real para la contraseña
    private fun setupPasswordValidation() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                // Si la contraseña tiene menos de 6 caracteres, mostramos el error
                if (password.length < 6) {
                    binding.tvPasswordError.visibility = View.VISIBLE
                    binding.ilPassword.boxStrokeColor = resources.getColor(R.color.appColor) // Borde rojo
                } else {
                    binding.tvPasswordError.visibility = View.GONE
                    binding.ilPassword.boxStrokeColor = resources.getColor(R.color.white) // Borde blanco
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Configuración del cambio de visibilidad de la contraseña
    private fun setupPasswordVisibilityToggle() {
        // Detectar clics en el ícono del "ojo" para alternar la visibilidad de la contraseña
        binding.ilPassword.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible // Cambiar el estado de visibilidad

            if (isPasswordVisible) {
                binding.etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ilPassword.setEndIconDrawable(R.drawable.ic_star) // Cambiar el icono a cerrado
            } else {
                binding.etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ilPassword.setEndIconDrawable(R.drawable.ic_eye_open) // Cambiar el icono a abierto
            }

            // Actualizar el campo de texto para reflejar el cambio en el inputType
            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener para el botón de login
        binding.loginButton.setOnClickListener {
            // Implementa aquí la lógica para realizar el login
        }

        // Listener para el botón de registro
        binding.registerButton.setOnClickListener {
            // Implementa la acción de registro aquí
        }
    }
}
