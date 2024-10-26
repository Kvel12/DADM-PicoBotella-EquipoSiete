package com.example.dadm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dadm.model.Challenge
import com.example.dadm.model.ProductModelResponse
import kotlinx.coroutines.launch


class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val challengeRepository = ChallengeRepository(context)


    private val _listChallenge = MutableLiveData<MutableList<Challenge>>()
    val listChallenge: LiveData<MutableList<Challenge>> get() = _listChallenge

    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    //para almacenar una lista de productos
    private val _listProducts = MutableLiveData<MutableList<ProductModelResponse>>()
    val listProducts: LiveData<MutableList<ProductModelResponse>> = _listProducts

    fun saveInventory(challenge: Challenge) {
        viewModelScope.launch {

            _progresState.value = true
            try {
                challengeRepository.saveInventory(challenge)
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
                _listChallenge.value = challengeRepository.getListInventory()
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
                challengeRepository.deleteInventory(challenge)
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
                challengeRepository.updateRepositoy(challenge)
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

    fun totalProducto(price: String, quantity: String): Double {
        val total = price * quantity
        return total.toDouble()
    }
}

