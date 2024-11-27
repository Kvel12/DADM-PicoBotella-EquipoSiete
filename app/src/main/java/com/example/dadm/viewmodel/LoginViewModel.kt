package com.example.dadm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.dadm.R

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // Validación de email
    val isEmailValid: LiveData<Boolean> = email.map {
        it?.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()) == true
    }

    // Validación de contraseña
    val isPasswordValid: LiveData<Boolean> = password.map {
        !it.isNullOrBlank() && it.length >= 6
    }

    // Control de errores de email
    val emailErrorVisible: LiveData<Boolean> = isEmailValid.map { !it }

    // Control de errores de contraseña
    val passwordErrorVisible: LiveData<Boolean> = isPasswordValid.map { !it }

    // Control del estado del botón de login
    private val _enableLoginButton = MediatorLiveData<Boolean>().apply {
        addSource(isEmailValid) { updateLoginButtonState() }
        addSource(isPasswordValid) { updateLoginButtonState() }
    }
    val enableLoginButton: LiveData<Boolean> get() = _enableLoginButton

    // Control del estado del botón de registro
    private val _enableRegisterButton = MediatorLiveData<Boolean>().apply {
        addSource(email) { updateRegisterButtonState() }
        addSource(password) { updateRegisterButtonState() }
    }
    val enableRegisterButton: LiveData<Boolean> get() = _enableRegisterButton

    private fun updateLoginButtonState() {
        _enableLoginButton.value = isEmailValid.value == true && isPasswordValid.value == true
    }

    private fun updateRegisterButtonState() {
        _enableRegisterButton.value =
            !email.value.isNullOrBlank() && !password.value.isNullOrBlank()
    }
}


