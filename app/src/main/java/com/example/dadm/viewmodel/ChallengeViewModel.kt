package com.example.dadm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dadm.model.Challenge
import com.example.dadm.model.ProductModelResponse
import com.example.dadm.repository.ChallengeRepository
import com.example.dadm.view.dialog.DialogoAgregarReto.mostrarDialogoAgregarReto
import com.example.dadm.view.dialog.DialogoMostrarReto.mostrarDialogoReto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val challengeRepository = ChallengeRepository(context)

    private val _listChallenge = MutableLiveData<MutableList<Challenge>>()
    val listChallenge: LiveData<MutableList<Challenge>> get() = _listChallenge

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    private val _estadoMostrarDialogo = MutableLiveData(false)
    val estadoMostrarDialogo: LiveData<Boolean> get() = _estadoMostrarDialogo

    private val _enableButton = MutableLiveData<Boolean>().apply { value = true }
    val enableButton: LiveData<Boolean> get() = _enableButton

    private val _statusRotationBottle = MutableLiveData<Boolean>().apply { value = false }
    val statusRotationBottle: LiveData<Boolean> get() = _statusRotationBottle

    private val _enabledStreamers = MutableLiveData(false)
    val enableStreamers: LiveData<Boolean> get() = _enabledStreamers

    private val _isMute = MutableLiveData(false)
    val isMute: LiveData<Boolean> get() = _isMute


    private val _statusShowDialog = MutableLiveData(false)
    val statusShowDialog: LiveData<Boolean> get() = _statusShowDialog

    private val _rotationBotle = MutableLiveData<RotateAnimation>()
    val rotationBottle: LiveData<RotateAnimation> get() = _rotationBotle

    private val _countdown = MutableLiveData<Int>()
    val countdown: LiveData<Int> get() = _countdown

    private var lastAngle = 0f // Variable para almacenar el último ángulo en el que se detuvo la botella




//    //para almacenar una lista de productos
//    private val _listProducts = MutableLiveData<MutableList<ProductModelResponse>>()
//    val listProducts: LiveData<MutableList<ProductModelResponse>> = _listProducts

    // LiveData que indica cuándo se debe iniciar la actividad principal
    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    fun splashScreen() {
        viewModelScope.launch {  // Inicia una corrutina
            delay(2000)          // Pausa la ejecución de la corrutina durante 2 segundos (2000 ms)
            _navigateToMain.value = true  // Cambia el valor de la variable LiveData para desencadenar la navegación
        }
    }


    // Reset del estado de navegación, si es necesario
    fun resetNavigation() {
        _navigateToMain.value = false
    }

    fun dialogoMostrarReto(
        context: Context,
        audioBackground: MediaPlayer,
        isMute: Boolean,
        messageChallenge: String,
    ){
        mostrarDialogoReto(context, audioBackground, isMute, messageChallenge)
    }


    fun estadoMostrarDialogo(estado: Boolean){
        _estadoMostrarDialogo.value = estado
    }

    suspend fun wait(time: Int){
        delay(time*1000L)
    }

    fun modifiSound(modify: Boolean){
        _isMute.value = modify
    }

    fun saveChallenge(challenge: Challenge){
        viewModelScope.launch { try {
            challengeRepository.saveChallenge(challenge)
        } catch (e: Exception){}}
    }

    fun getListChallenge(){
        viewModelScope.launch {
            try {
                _listChallenge.value = challengeRepository.getListChallenge()
            } catch (e: Exception) {

            }
        }
    }

    fun updateChallenge (challenge: Challenge){
        viewModelScope.launch {
            _progresState.value = true
            try {
                challengeRepository.updateChallenge(challenge)
            } catch (e: Exception) {
            }

        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                challengeRepository.deleteChallenge(challenge)

            } catch (e: Exception) {

            }

        }
    }

    fun spinBottle() {
        _statusRotationBottle.value = true

        // Generar un nuevo ángulo de rotación aleatorio (1080-3600 grados) y agregar el último ángulo
        val degrees = (Math.random() * 3600 + 1080).toFloat()
        val finalAngle = lastAngle + degrees // el ángulo total de rotación

        // Crear la animación de rotación desde lastAngle hasta finalAngle
        val rotation = RotateAnimation(
            lastAngle, finalAngle,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            fillAfter = true // Mantener la posición final
            duration = 3600
            interpolator = DecelerateInterpolator()


            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    _enabledStreamers.value = true
                    _enableButton.value = false // Desactivar botón
                }

                override fun onAnimationEnd(animation: Animation?) {
                    _statusRotationBottle.value = false
//                    _enabledStreamers.value = false
//                    _statusShowDialog.value = true // Activar el diálogo
                    lastAngle = finalAngle % 360 // Actualizar el último ángulo (en rango de 0 a 360 grados)

                    // Iniciar cuenta regresiva de 3 a 0 después de que se detiene la botella
                    startCountdown()

                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        // Asignar la animación a LiveData para que se observe en la vista
        _rotationBotle.value = rotation
    }

    private fun startCountdown() {
        viewModelScope.launch {
            for (i in 3 downTo 0) {
                _countdown.value = i
                delay(1000) // Espera de 1 segundo entre cada número
            }
            // Una vez llega a 0, mostrar el cuadro de diálogo (Criterio 6)
            _statusShowDialog.value = true
            _enableButton.value = true // Habilita el botón nuevamente al terminar la cuenta regresiva
        }
    }


    fun setStatusShowDialog(show: Boolean) {
        _statusShowDialog.value = show
    }

    // Reinicia el valor de _statusShowDialog después de mostrar el cuadro de diálogo en el Fragment
    fun resetStatusShowDialog() {
        _statusShowDialog.value = false
    }



}


