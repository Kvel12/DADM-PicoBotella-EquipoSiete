package com.example.dadm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.media.MediaPlayer
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

    private val _ListaReto = MutableLiveData<MutableList<Challenge>>()
    val listaReto: LiveData<MutableList<Challenge>> get() = _ListaReto

    //para almacenar una lista de productos
    private val _listProducts = MutableLiveData<MutableList<ProductModelResponse>>()
    val listProducts: LiveData<MutableList<ProductModelResponse>> = _listProducts

    // LiveData que indica cuándo se debe iniciar la actividad principal
    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    fun splashScreen() {
        viewModelScope.launch {
            delay(2000) // Espera el tiempo definido
            _navigateToMain.value = true // Cambia el valor para indicar a la vista que debe navegar
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

    fun saveChallenge(challenge: Challenge){
        viewModelScope.launch { try {
            challengeRepository.saveChallenge(challenge)
        } catch (e: Exception){}}
    }

    fun getListChallenge(){
        viewModelScope.launch {
            try {
                challengeRepository.getListChallenge()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun updateChallenge (challenge: Challenge){
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listChallenge.value = challengeRepository.updateChallenge(challenge)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                challengeRepository.deleteChallenge(challenge)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun updateInventory(challenge: Challenge) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                challengeRepository.updateChallenge(challenge)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

}


