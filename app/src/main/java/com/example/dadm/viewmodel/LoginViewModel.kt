package com.example.dadm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.dadm.model.UserRequest
import com.example.dadm.model.UserResponse
import com.example.dadm.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val repository = LoginRepository()
    private val _isRegister = MutableLiveData<UserResponse>()
    val isRegister: LiveData<UserResponse> = _isRegister

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val isEmailValid: LiveData<Boolean> = email.map {
        it?.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()) == true
    }

    val isPasswordValid: LiveData<Boolean> = password.map {
        !it.isNullOrBlank() && it.length >= 6
    }

    val emailErrorVisible: LiveData<Boolean> = isEmailValid.map { !it }
    val passwordErrorVisible: LiveData<Boolean> = isPasswordValid.map { !it }

    private val _enableLoginButton = MediatorLiveData<Boolean>().apply {
        addSource(isEmailValid) { updateLoginButtonState() }
        addSource(isPasswordValid) { updateLoginButtonState() }
    }
    val enableLoginButton: LiveData<Boolean> get() = _enableLoginButton

    private val _enableRegisterButton = MediatorLiveData<Boolean>().apply {
        addSource(email) { updateRegisterButtonState() }
        addSource(password) { updateRegisterButtonState() }
    }
    val enableRegisterButton: LiveData<Boolean> get() = _enableRegisterButton

    fun registerUser(userRequest: UserRequest) {
        if (isEmailValid.value == true && isPasswordValid.value == true) {
            repository.registerUser(userRequest) { userResponse ->
                _isRegister.postValue(userResponse)
            }
        } else {
            // Mostrar mensaje de error si los campos no son válidos
            _isRegister.postValue(
                UserResponse(
                    isRegister = false,
                    message = "Por favor, verifica los campos ingresados"
                )
            )
        }
    }

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {
        if (isEmailValid.value == true && isPasswordValid.value == true) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isLogin(true)
                    } else {
                        // Mostrar mensaje de login incorrecto
                        isLogin(false)
                    }
                }
        } else {
            // Si el email o la contraseña no son válidos
            isLogin(false)
        }
    }

    fun sesion(email: String?, isEnableView: (Boolean) -> Unit) {
        isEnableView(email != null)
    }

    private fun updateLoginButtonState() {
        _enableLoginButton.value = isEmailValid.value == true && isPasswordValid.value == true
    }

    private fun updateRegisterButtonState() {
        _enableRegisterButton.value = !email.value.isNullOrBlank() && !password.value.isNullOrBlank()
    }
}