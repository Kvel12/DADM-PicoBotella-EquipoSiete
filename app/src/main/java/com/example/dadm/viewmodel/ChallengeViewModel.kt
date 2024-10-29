package com.example.dadm.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dadm.model.Challenge
import com.example.dadm.model.ProductModelResponse
import com.example.dadm.repository.ChallengeRepository
import com.example.dadm.utils.Constants.TIME
import com.example.dadm.view.MainActivity
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

    //para almacenar una lista de productos
    private val _listProducts = MutableLiveData<MutableList<ProductModelResponse>>()
    val listProducts: LiveData<MutableList<ProductModelResponse>> = _listProducts

    // LiveData que indica cuándo se debe iniciar la actividad principal
    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    fun splashScreen() {
        viewModelScope.launch {
            delay(TIME) // Espera el tiempo definido en `Constants.TIME`
            _navigateToMain.value = true // Cambia el valor para indicar a la vista que debe navegar
        }
    }

    // Reset del estado de navegación, si es necesario
    fun resetNavigation() {
        _navigateToMain.value = false
    }

    fun saveInventory(challenge: Challenge) {
        viewModelScope.launch {

            _progresState.value = true
            try {
                challengeRepository.saveChallenge(challenge)
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

    fun getListInventory() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listChallenge.value = challengeRepository.getListChallenge()
                _progresState.value = false
            } catch (e: Exception) {
                _progresState.value = false
            }

        }
    }

    fun deleteInventory(challenge: Challenge) {
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

    fun getProducts() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listProducts.value = challengeRepository.getProducts()
                _progresState.value = false

            } catch (e: Exception) {
                _progresState.value = false
            }
        }
    }

//    fun totalProducto(price: String, quantity: String): Double {
//        val total = price * quantity
//        return total.toDouble()
//    }
}

