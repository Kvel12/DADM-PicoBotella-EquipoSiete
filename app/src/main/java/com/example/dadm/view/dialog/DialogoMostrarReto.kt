package com.example.dadm.view.dialog

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.dadm.databinding.DialogShowChallengeBinding

object DialogoMostrarReto {

    // Función para mostrar un diálogo con un mensaje de reto.
    fun mostrarDialogoReto(
        contexto: Context,              // Contexto de la actividad o fragmento.
        audioFondo: MediaPlayer,        // Objeto MediaPlayer para reproducir audio de fondo.
        estaSilenciado: Boolean,        // Booleano que indica si el audio está en silencio.
        mensajeReto: String             // Mensaje del reto que se va a mostrar en el diálogo.
    ) {
        // Inflador para crear la vista del diálogo a partir del layout 'DialogShowChallengeBinding'.
        val inflador = LayoutInflater.from(contexto)
        val bindingDialogo = DialogShowChallengeBinding.inflate(inflador)

        // Creación del cuadro de diálogo de alerta.
        val dialogoAlerta = AlertDialog.Builder(contexto).create()
        // Configura el fondo del cuadro de diálogo como transparente.
        dialogoAlerta.window?.setBackgroundDrawableResource(android.R.color.transparent)
        // Hace que el cuadro de diálogo no sea cancelable al tocar fuera de él.
        dialogoAlerta.setCancelable(false)
        // Establece la vista inflada en el diálogo.
        dialogoAlerta.setView(bindingDialogo.root)

        // Asigna el mensaje del reto al TextView en el layout del diálogo.
        bindingDialogo.tvChagenlle.text = mensajeReto

        // Configura el botón 'Cerrar' para que reproduzca el audio (si no está en silencio) y cierre el diálogo.
        bindingDialogo.btnClose.setOnClickListener {
            // Si el audio no está en silencio, lo reproduce.
            if (!estaSilenciado) {
                audioFondo.start()
            }
            // Cierra el cuadro de diálogo.
            dialogoAlerta.dismiss()
        }

        // Muestra el cuadro de diálogo en pantalla.
        dialogoAlerta.show()
    }

}