package com.example.dadm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // Validar si el formulario está completo
    val isFormValid: LiveData<Boolean> = email.map {
        !it.isNullOrBlank() && !password.value.isNullOrBlank()
    }
}
