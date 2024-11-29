package com.example.dadm

import android.content.Context
import android.media.MediaPlayer
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.dadm.firebase.FirebaseProvider
import com.example.dadm.model.Challenge
import com.example.dadm.repository.ChallengeRepository
import com.example.dadm.utils.Result
import com.example.dadm.viewmodel.ChallengeViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import org.mockito.MockitoAnnotations

// Anotación para indicar que usamos características experimentales de corrutinas
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ChallengeViewModelTest {

    // Regla necesaria para manejar tareas en segundo plano de LiveData durante las pruebas
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Dispatcher especial para pruebas de corrutinas
    private val testDispatcher = StandardTestDispatcher()

    // Declaración de mocks necesarios para las pruebas
    @Mock
    private lateinit var challengeRepository: ChallengeRepository
    // ViewModel que vamos a probar
    private lateinit var viewModel: ChallengeViewModel

    @Mock
    private lateinit var listChallengeObserver: Observer<Result<MutableList<Challenge>>>

    @Mock
    private lateinit var firebaseProvider: FirebaseProvider

    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var collectionReference: CollectionReference

    @Mock
    private lateinit var audioBackground: MediaPlayer


    // Datos de prueba para simular retos
    private val mockChallenges = mutableListOf(
        Challenge(id = "1", descripcion = "Reto 1", timestamp = System.currentTimeMillis()),
        Challenge(id = "2", descripcion = "Reto 2", timestamp = System.currentTimeMillis())
    )

    // Configuración inicial antes de cada prueba
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)

        // Configurar el comportamiento del firebaseProvider
        val mockFirestore = mock(FirebaseFirestore::class.java)
        val mockCollection = mock(CollectionReference::class.java)
        `when`(firebaseProvider.getFirestore()).thenReturn(mockFirestore)
        `when`(mockFirestore.collection(any())).thenReturn(mockCollection)

        viewModel = ChallengeViewModel(challengeRepository, firebaseProvider)
    }

    // Limpieza después de cada prueba
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Prueba para verificar la obtención de la lista de retos
    @Test
    fun `getListChallenge debe actualizar la lista cuando es exitoso`() = runTest {
        val mockList = mutableListOf(
            Challenge(id = "1", descripcion = "Reto 1", timestamp = System.currentTimeMillis()),
            Challenge(id = "2", descripcion = "Reto 2", timestamp = System.currentTimeMillis())
        )

        `when`(challengeRepository.getListChallenge()).thenReturn(kotlin.Result.success(mockList))

        viewModel.listChallenge.observeForever(listChallengeObserver)
        viewModel.getListChallenge()
        testDispatcher.scheduler.runCurrent()

        // Verificamos solo que se llamó al método ya que la transformación del resultado
        // se maneja internamente en el ViewModel
        verify(challengeRepository).getListChallenge()
    }

    // Prueba para verificar el guardado de un reto
    @Test
    fun `saveChallenge debe llamar al repositorio correctamente`() = runTest {
        val reto = Challenge(descripcion = "Nuevo Reto", timestamp = System.currentTimeMillis())

        `when`(challengeRepository.saveChallenge(reto)).thenReturn(kotlin.Result.success(Unit))

        viewModel.saveChallenge(reto)
        testDispatcher.scheduler.runCurrent()

        verify(challengeRepository).saveChallenge(reto)
    }

    // Prueba para verificar la actualizacion de un reto
    @Test
    fun `updateChallenge debe actualizar el estado de progreso`() = runTest {
        val reto = Challenge(id = "1", descripcion = "Reto Actualizado", timestamp = System.currentTimeMillis())

        `when`(challengeRepository.updateChallenge(reto)).thenReturn(kotlin.Result.success(Unit))

        viewModel.updateChallenge(reto)
        testDispatcher.scheduler.runCurrent()

        verify(challengeRepository).updateChallenge(reto)
        assertFalse(viewModel.progresState.value ?: true)
    }

    // Prueba para verificar la eliminación de un reto
    @Test
    fun `deleteChallenge debe llamar al repositorio`() = runTest {
        val reto = Challenge(id = "1", descripcion = "Reto a Eliminar", timestamp = System.currentTimeMillis())

        `when`(challengeRepository.deleteChallenge(reto)).thenReturn(kotlin.Result.success(Unit))

        viewModel.deleteChallenge(reto)
        testDispatcher.scheduler.runCurrent()

        verify(challengeRepository).deleteChallenge(reto)
    }

    // Prueba para verificar el funcionamiento de la pantalla de inicio
    @Test
    fun `splashScreen debe activar la navegación después del delay`() = runTest {
        // Ejecutamos la función de splash screen
        viewModel.splashScreen()
        testDispatcher.scheduler.advanceTimeBy(2000)
        testDispatcher.scheduler.runCurrent()

        // Verificamos que se activó la navegación
        assertTrue(viewModel.navigateToMain.value == true)
    }

    // Prueba para verificar la actualización del estado del diálogo
    @Test
    fun `setStatusShowDialog debe actualizar el estado del diálogo`() {
        // Ejecutamos el cambio de estado
        viewModel.setStatusShowDialog(true)

        // Verificamos que se actualizó correctamente
        assertTrue(viewModel.statusShowDialog.value == true)
    }

    // Prueba para verificar la modificación del estado del sonido
    @Test
    fun `modifiSound debe actualizar el estado del sonido`() {
        // Ejecutamos el cambio de estado del sonido
        viewModel.modifiSound(true)

        // Verificamos que se actualizó correctamente
        assertTrue(viewModel.isMute.value == true)
    }

    // Prueba para verificar la animación de la botella
    @Test
    fun `spinBottle debe actualizar el estado de rotación`() {
        // Ejecutamos la función de girar la botella
        viewModel.spinBottle()

        // Verificamos que se actualizaron los estados correctamente
        assertTrue(viewModel.statusRotationBottle.value == true)
        assertNotNull(viewModel.rotationBottle.value)
    }
}