package com.example.dadm.view

import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dadm.R
import com.example.dadm.databinding.FragmentLoginBinding
import com.example.dadm.model.UserRequest
import com.example.dadm.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSession()
        setupUI()
        setupObservers()

        if (FirebaseAuth.getInstance().currentUser != null) {
            // Si hay un usuario autenticado, navega al fragmento de inicio
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

    }

    private fun setupUI() {
        setupEmailValidation()
        setupPasswordValidation()
        setupPasswordVisibilityToggle()
        setupButtonStates()
        setupButtons()
    }

    private fun setupButtons() {
        binding.loginButton.setOnClickListener {
            handleLogin()
        }

        binding.registerButton.setOnClickListener {
            handleRegister()
            setupRegisterButtonAnimation()
        }
    }

    private fun setupEmailValidation() {
        loginViewModel.emailErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvEmailError.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    private fun setupPasswordValidation() {
        loginViewModel.passwordErrorVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvPasswordError.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    private fun setupPasswordVisibilityToggle() {
        binding.ilPassword.setEndIconOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.ilPassword.setEndIconDrawable(R.drawable.ic_eye_open)
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.ilPassword.setEndIconDrawable(R.drawable.ic_eye_closed)
            }
            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
        }
    }

    private fun setupButtonStates() {
        loginViewModel.enableLoginButton.observe(viewLifecycleOwner) { isEnabled ->
            binding.loginButton.isEnabled = isEnabled
            binding.loginButton.setTextColor(
                if (isEnabled) requireContext().getColor(R.color.white)
                else requireContext().getColor(R.color.app_red)
            )
        }

        loginViewModel.enableRegisterButton.observe(viewLifecycleOwner) { isEnabled ->
            binding.registerButton.isEnabled = isEnabled
            binding.registerButton.setTextColor(
                if (isEnabled) requireContext().getColor(R.color.white)
                else requireContext().getColor(R.color.app_gray)
            )
        }
    }

    private fun setupObservers() {
        loginViewModel.isRegister.observe(viewLifecycleOwner) { userResponse ->
            if (userResponse.isRegister) {
                Toast.makeText(requireContext(), userResponse.message, Toast.LENGTH_SHORT).show()
                saveEmailToPreferences(userResponse.email)
                navigateToHome()
            } else {
                Toast.makeText(requireContext(), "Error en el registro", Toast.LENGTH_SHORT).show() // Mensaje ajustado
            }
        }
    }


    private fun handleLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.loginUser(email, password) { isLogin ->
                if (isLogin) {
                    saveEmailToPreferences(email)
                    navigateToHome()
                } else {
                    Toast.makeText(requireContext(), "Login incorrecto", Toast.LENGTH_SHORT).show() // Mensaje ajustado
                }
            }
        }
    }


    private fun handleRegister() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            val userRequest = UserRequest(email, password)
            loginViewModel.registerUser(userRequest)
        } else {
            Toast.makeText(requireContext(), "Campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupRegisterButtonAnimation() {
        val scaleAnimation = ScaleAnimation(
            1f, 0.95f,
            1f, 0.95f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 150
            repeatCount = 1
            repeatMode = Animation.REVERSE
        }
        binding.registerButton.startAnimation(scaleAnimation)
    }

    private fun checkSession() {
        val email = requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
            .getString("email", null)

        loginViewModel.sesion(email) { isEnableView ->
            if (isEnableView) {
                binding.clContenedor.visibility = View.INVISIBLE
                navigateToHome()
            }
        }
    }

    private fun saveEmailToPreferences(email: String?) {
        requireActivity().getSharedPreferences("shared", Context.MODE_PRIVATE)
            .edit()
            .putString("email", email)
            .apply()
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }
}