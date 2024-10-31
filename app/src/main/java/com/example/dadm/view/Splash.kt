package com.example.dadm.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.dadm.R
import com.example.dadm.view.MainActivity
import com.example.dadm.viewmodel.ChallengeViewModel

// Clase Splash para mostrar la pantalla de bienvenida al iniciar la app
class Splash : AppCompatActivity() {

    // Inicializa una instancia de ChallengeViewModel usando el patrón viewModels()
    private val challengeViewModel: ChallengeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Establece el diseño de la actividad

        // Configuración para que la actividad esté en pantalla completa
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Para Android 11 y versiones superiores
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            controller?.apply {
                // Oculta la barra de estado y la barra de navegación
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                // Permite mostrar las barras temporalmente con un gesto de deslizamiento
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Para versiones de Android anteriores a la 11 (usando métodos depreciados)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        // Observador de LiveData para la navegación a MainActivity cuando esté listo
        challengeViewModel.navigateToMain.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                // Inicia la actividad principal (MainActivity)
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Finaliza la actividad Splash para que no se pueda volver a ella
                challengeViewModel.resetNavigation() // Restaura el estado de navegación a false
            }
        })

        // Llama a la función splashScreen() en el ViewModel para iniciar el temporizador o carga inicial
        challengeViewModel.splashScreen()

        // Configuración para que el botón de retroceso no haga nada en esta pantalla
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Vacío para deshabilitar el botón de retroceso mientras está en la pantalla Splash
            }
        })
    }
}
