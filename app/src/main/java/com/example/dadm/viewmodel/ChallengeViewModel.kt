package com.example.dadm.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dadm.firebase.FirebaseProvider
import com.example.dadm.model.Challenge
import com.example.dadm.repository.ChallengeRepository
import com.example.dadm.view.dialog.DialogoMostrarReto.mostrarDialogoReto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.dadm.utils.Result

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val firebaseProvider: FirebaseProvider
) : ViewModel() {

    companion object {
        private const val CHALLENGES_COLLECTION = "challenges"
    }

    // Instancia de Firestore
    private val db by lazy { firebaseProvider.getFirestore() }
    private val challengesCollection by lazy { db.collection(CHALLENGES_COLLECTION) }

    // LiveData para la lista de retos
    private val _listChallenge = MutableLiveData<Result<MutableList<Challenge>>>()
    val listChallenge: LiveData<Result<MutableList<Challenge>>> = _listChallenge

    // LiveData para estados de UI y animación
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

    // Variable para el último ángulo de rotación
    private var lastAngle = 0f

    // LiveData para errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // LiveData para navegación
    private val _navigateToMain = MutableLiveData<Boolean>()
    val navigateToMain: LiveData<Boolean> get() = _navigateToMain

    // Función para guardar un reto en Firebase
    fun saveChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                // Crear nuevo documento en Firebase
                val newDoc = challengesCollection.document()
                val challengeWithId = challenge.copy(
                    id = newDoc.id,
                    timestamp = System.currentTimeMillis()
                )

                newDoc.set(challengeWithId.toMap())
                    .addOnSuccessListener {
                        // Actualizar la lista después de guardar
                        getListChallenge()
                    }
                    .addOnFailureListener { e ->
                        _error.value = "Error al guardar el reto: ${e.message}"
                    }
            } catch (e: Exception) {
                _error.value = "Error inesperado: ${e.message}"
            }
        }
    }

    // Función para obtener la lista de retos desde Firebase
    fun getListChallenge() {
        viewModelScope.launch {
            try {
                challengesCollection
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { documents ->
                        val challenges = documents.mapNotNull { doc ->
                            doc.toObject(Challenge::class.java)?.copy(id = doc.id)
                        }.toMutableList()
                        _listChallenge.value = Result.Success(challenges)
                    }
                    .addOnFailureListener { e ->
                        _listChallenge.value = Result.Failure(e as Exception)
                    }
            } catch (e: Exception) {
                _listChallenge.value = Result.Failure(e)
            }
        }
    }

    // Función para actualizar un reto en Firebase
    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                challengesCollection.document(challenge.id)
                    .set(challenge.toMap())
                    .addOnSuccessListener {
                        getListChallenge()
                        _progresState.value = false
                    }
                    .addOnFailureListener { e ->
                        _error.value = "Error al actualizar el reto: ${e.message}"
                        _progresState.value = false
                    }
            } catch (e: Exception) {
                _error.value = "Error inesperado: ${e.message}"
                _progresState.value = false
            }
        }
    }

    // Función para eliminar un reto de Firebase
    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            try {
                challengesCollection.document(challenge.id)
                    .delete()
                    .addOnSuccessListener {
                        getListChallenge()
                    }
                    .addOnFailureListener { e ->
                        _error.value = "Error al eliminar el reto: ${e.message}"
                    }
            } catch (e: Exception) {
                _error.value = "Error inesperado: ${e.message}"
            }
        }
    }

    // Funciones de la animación de la botella y control de audio
    fun createRotationAnimation(fromAngle: Float, toAngle: Float): RotateAnimation {
        return RotateAnimation(
            fromAngle, toAngle,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            fillAfter = true
            duration = 3600
            interpolator = DecelerateInterpolator()

            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    _enabledStreamers.value = true
                    _enableButton.value = false
                }

                override fun onAnimationEnd(animation: Animation?) {
                    _statusRotationBottle.value = false
                    lastAngle = toAngle % 360
                    startCountdown()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    // Función para girar la botella
    fun spinBottle() {
        _statusRotationBottle.value = true
        val degrees = (Math.random() * 3600 + 1080).toFloat()
        val finalAngle = lastAngle + degrees
        val rotation = createRotationAnimation(lastAngle, finalAngle)
        _rotationBotle.value = rotation
    }

    // Función para la cuenta regresiva
    private fun startCountdown() {
        viewModelScope.launch {
            for (i in 3 downTo 0) {
                _countdown.value = i
                delay(1000)
            }
            _statusShowDialog.value = true
            _enableButton.value = true
        }
    }

    // Funciones de control de diálogo
    fun setStatusShowDialog(show: Boolean) {
        _statusShowDialog.value = show
    }

    fun resetStatusShowDialog() {
        _statusShowDialog.value = false
    }

    // Funciones para el manejo de diálogos y audio
    fun dialogoMostrarReto(
        context: Context,
        audioBackground: MediaPlayer,
        isMute: Boolean,
        messageChallenge: String
    ) {
        mostrarDialogoReto(context, audioBackground, isMute, messageChallenge)
    }

    fun estadoMostrarDialogo(estado: Boolean) {
        _estadoMostrarDialogo.value = estado
    }

    suspend fun wait(time: Int) {
        delay(time * 1000L)
    }

    fun modifiSound(modify: Boolean) {
        _isMute.value = modify
    }

    // Funciones de navegación
    fun splashScreen() {
        viewModelScope.launch {
            delay(2000)
            _navigateToMain.value = true
        }
    }

    fun resetNavigation() {
        _navigateToMain.value = false
    }
}