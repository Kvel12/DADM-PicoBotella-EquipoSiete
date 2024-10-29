package com.example.dadm.viewmodel

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


class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
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
                _ListaReto.value = challengeRepository.getListChallenge()
            } catch (e: Exception){}
        }
    }

    fun deleteChallenge(challenge: Challenge){
        viewModelScope.launch {
            try {
                challengeRepository.deleteChallenge(challenge)
            }catch (e: Exception){

            }
        }
    }

    fun updateChallenge (challenge: Challenge){
        viewModelScope.launch {
            try{
                challengeRepository.updateChallenge(challenge)
            }catch (e: Exception){

            }
        }
    }


}

