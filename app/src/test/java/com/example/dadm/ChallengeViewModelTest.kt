package com.example.dadm

import android.content.Context
import android.media.MediaPlayer
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.dadm.model.Challenge
import com.example.dadm.repository.ChallengeRepository
import com.example.dadm.viewmodel.ChallengeViewModel
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
import org.mockito.ArgumentCaptor
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ChallengeViewModelTest {

    // Rule para manejar LiveData de manera síncrona en pruebas
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Dispatcher de pruebas para corrutinas
    private val testDispatcher = StandardTestDispatcher()

    // Mocks para dependencias
    @Mock
    private lateinit var challengeRepository: ChallengeRepository

    @Mock
    private lateinit var listChallengeObserver: Observer<MutableList<Challenge>>

    @Mock
    private lateinit var randomChallengeObserver: Observer<Challenge?>

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var audioBackground: MediaPlayer

    // El ViewModel que vamos a probar
    private lateinit var viewModel: ChallengeViewModel

    @Before
    fun setUp() {
        // Configurar el dispatcher principal para pruebas
        Dispatchers.setMain(testDispatcher)

        // Inicializar el ViewModel con el repositorio mockeado
        viewModel = ChallengeViewModel(challengeRepository)
    }

    @After
    fun tearDown() {
        // Restablecer el dispatcher principal
        Dispatchers.resetMain()
    }

    @Test
    fun `getListChallenge debe obtener y actualizar la lista de retos`() = runTest {
        // Preparar datos de prueba
        val mockChallenges = mutableListOf(
            Challenge(id = 1, descripcion = "Reto 1"),
            Challenge(id = 2, descripcion = "Reto 2")
        )

        // Configurar comportamiento del repositorio mockeado
        `when`(challengeRepository.getListChallenge()).thenReturn(mockChallenges)

        // Observar el LiveData
        viewModel.listChallenge.observeForever(listChallengeObserver)

        // Ejecutar el método a probar
        viewModel.getListChallenge()

        // Avanzar el dispatcher para ejecutar la corrutina
        testDispatcher.scheduler.runCurrent()

        // Verificar que se actualizó la lista de retos
        verify(listChallengeObserver).onChanged(mockChallenges)

        // Verificar que la lista en el ViewModel coincide
        assertEquals(mockChallenges, viewModel.listChallenge.value)
    }

    @Test
    fun `getRandomChallenge debe seleccionar un reto aleatorio de la lista`() {
        // Crear lista de retos simulada
        val mockChallenges = mutableListOf(
            Challenge(id = 1, descripcion = "Reto 1"),
            Challenge(id = 2, descripcion = "Reto 2"),
            Challenge(id = 3, descripcion = "Reto 3")
        )

        // Configurar la lista de retos en el ViewModel usando reflexión
        val listChallengeField = viewModel.javaClass.getDeclaredField("_listChallenge")
        listChallengeField.isAccessible = true
        val mutableLiveDataMethod = listChallengeField.get(viewModel).javaClass.getMethod("setValue", Any::class.java)
        mutableLiveDataMethod.invoke(listChallengeField.get(viewModel), mockChallenges)

        // Registrar el Observer en randomChallenge
        viewModel.randomChallenge.observeForever(randomChallengeObserver)

        // Ejecutar el método a probar
        viewModel.getRandomChallenge()

        // Capturar el cambio en el LiveData
        val argumentCaptor = ArgumentCaptor.forClass(Challenge::class.java)
        verify(randomChallengeObserver).onChanged(argumentCaptor.capture())

        // Validar que el reto seleccionado no es nulo y pertenece a la lista original
        val selectedChallenge = argumentCaptor.value
        assertNotNull(selectedChallenge)
        assertTrue(mockChallenges.contains(selectedChallenge))
    }

    @Test
    fun `saveChallenge debe guardar un reto en el repositorio`() = runTest {
        // Preparar un reto de prueba
        val testChallenge = Challenge(id = 1, descripcion = "Reto de prueba")

        // Ejecutar el método a probar
        viewModel.saveChallenge(testChallenge)

        // Avanzar el dispatcher para ejecutar la corrutina
        testDispatcher.scheduler.runCurrent()

        // Verificar que se llamó al método saveChallenge del repositorio
        verify(challengeRepository).saveChallenge(testChallenge)
    }

    @Test
    fun `updateChallenge debe actualizar un reto en el repositorio`() = runTest {
        // Preparar un reto de prueba
        val testChallenge = Challenge(id = 1, descripcion = "Reto actualizado")

        // Ejecutar el método a probar
        viewModel.updateChallenge(testChallenge)

        // Avanzar el dispatcher para ejecutar la corrutina
        testDispatcher.scheduler.runCurrent()

        // Verificar que se llamó al método updateChallenge del repositorio
        verify(challengeRepository).updateChallenge(testChallenge)

        // Verificar que el estado de progreso cambió
        assertTrue(viewModel.progresState.value == true)
    }

    @Test
    fun `deleteChallenge debe eliminar un reto del repositorio`() = runTest {
        // Preparar un reto de prueba
        val testChallenge = Challenge(id = 1, descripcion = "Reto a eliminar")

        // Ejecutar el método a probar
        viewModel.deleteChallenge(testChallenge)

        // Avanzar el dispatcher para ejecutar la corrutina
        testDispatcher.scheduler.runCurrent()

        // Verificar que se llamó al método deleteChallenge del repositorio
        verify(challengeRepository).deleteChallenge(testChallenge)
    }

    @Test
    fun `splashScreen debe activar navegación después de 2 segundos`() = runTest {
        // Registrar un Observer para navegar
        val navigationObserver = mock(Observer::class.java) as Observer<Boolean>
        viewModel.navigateToMain.observeForever(navigationObserver)

        // Ejecutar el método a probar
        viewModel.splashScreen()

        // Avanzar el tiempo del dispatcher para simular el retraso
        testScheduler.advanceTimeBy(2000L)

        // Procesar eventos pendientes
        testScheduler.runCurrent()

        // Verificar que la navegación fue activada
        verify(navigationObserver).onChanged(true)
        assertTrue(viewModel.navigateToMain.value == true)
    }

    @Test
    fun `resetNavigation debe establecer navegación a false`() {
        // Primero activar la navegación
        val navField = viewModel.javaClass.getDeclaredField("_navigateToMain")
        navField.isAccessible = true
        val mutableLiveDataMethod = navField.get(viewModel).javaClass.getMethod("setValue", Any::class.java)
        mutableLiveDataMethod.invoke(navField.get(viewModel), true)

        // Resetear navegación
        viewModel.resetNavigation()

        // Verificar que está en false
        assertFalse(viewModel.navigateToMain.value == true)
    }

    @Test
    fun `estadoMostrarDialogo debe cambiar el estado del diálogo`() {
        // Probar estableciendo el estado a true
        viewModel.estadoMostrarDialogo(true)
        assertTrue(viewModel.estadoMostrarDialogo.value == true)

        // Probar estableciendo el estado a false
        viewModel.estadoMostrarDialogo(false)
        assertFalse(viewModel.estadoMostrarDialogo.value == true)
    }

    @Test
    fun `modifiSound debe cambiar el estado de silencio`() {
        // Probar estableciendo mute a true
        viewModel.modifiSound(true)
        assertTrue(viewModel.isMute.value == true)

        // Probar estableciendo mute a false
        viewModel.modifiSound(false)
        assertFalse(viewModel.isMute.value == true)
    }

    @Test
    fun `setStatusShowDialog debe cambiar el estado del diálogo`() {
        // Probar estableciendo el estado a true
        viewModel.setStatusShowDialog(true)
        assertTrue(viewModel.statusShowDialog.value == true)

        // Probar estableciendo el estado a false
        viewModel.setStatusShowDialog(false)
        assertFalse(viewModel.statusShowDialog.value == true)
    }

    @Test
    fun `resetStatusShowDialog debe establecer el estado del diálogo a false`() {
        // Primero establecer el estado a true
        val statusField = viewModel.javaClass.getDeclaredField("_statusShowDialog")
        statusField.isAccessible = true
        val mutableLiveDataMethod = statusField.get(viewModel).javaClass.getMethod("setValue", Any::class.java)
        mutableLiveDataMethod.invoke(statusField.get(viewModel), true)

        // Resetear el estado
        viewModel.resetStatusShowDialog()

        // Verificar que está en false
        assertFalse(viewModel.statusShowDialog.value == true)
    }

    @Test
    fun `obtenerRetoAleatorio debe retornar un reto aleatorio de la lista`() {
        // Preparar datos de prueba
        val mockChallenges = mutableListOf(
            Challenge(id = 1, descripcion = "Reto 1"),
            Challenge(id = 2, descripcion = "Reto 2"),
            Challenge(id = 3, descripcion = "Reto 3")
        )

        // Usar reflection para establecer el valor de _listChallenge
        val listChallengeField = viewModel.javaClass.getDeclaredField("_listChallenge")
        listChallengeField.isAccessible = true
        val mutableLiveDataMethod = listChallengeField.get(viewModel).javaClass.getMethod("setValue", Any::class.java)
        mutableLiveDataMethod.invoke(listChallengeField.get(viewModel), mockChallenges)

        // Ejecutar el método
        val randomChallenge = viewModel.obtenerRetoAleatorio()

        // Verificar que el reto seleccionado no es nulo y está en la lista
        assertNotNull(randomChallenge)
        assertTrue(mockChallenges.contains(randomChallenge))
    }



}