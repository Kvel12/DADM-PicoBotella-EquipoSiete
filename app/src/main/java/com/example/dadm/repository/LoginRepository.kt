package com.example.dadm.repository

import com.example.dadm.model.UserRequest
import com.example.dadm.model.UserResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class LoginRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    fun registerUser(userRequest: UserRequest, userResponse: (UserResponse) -> Unit) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(userRequest.email, userRequest.password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        val email = task.result?.user?.email
                        userResponse(
                            UserResponse(
                                email =  task.result?.user?.email,
                                isRegister = true,
                                message = "Registro Exitoso"
                            )
                        )
                    } else {
                        val error = task.exception
                        if (error is FirebaseAuthUserCollisionException) {
                            userResponse(
                                UserResponse(
                                    isRegister = false,
                                    message = "Error en el registro" // Mensaje ajustado
                                )
                            )
                        } else {
                            userResponse(
                                UserResponse(
                                    isRegister = false,
                                    message = "Error en el registro" // Mensaje ajustado
                                )
                            )
                        }
                    }
                }
        } catch (e: Exception) {
            // Manejo de excepciones generales
            userResponse(
                UserResponse(
                    isRegister = false,
                    message = e.message ?: "Error desconocido"
                )
            )
        }
    }
}