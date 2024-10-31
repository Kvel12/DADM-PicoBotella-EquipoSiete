package com.example.dadm.view.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.dadm.databinding.DialogUpdateChallengeBinding
import com.example.dadm.model.Challenge
import com.example.dadm.viewmodel.ChallengeViewModel

object DialogoEditarReto {

    // Función para mostrar un diálogo que permite editar un reto existente.
    fun mostrarDialogoEditarReto(
        context: Context,                    // Contexto de la actividad o fragmento.
        challengeViewModel: ChallengeViewModel, // ViewModel que maneja la lógica y los datos del reto.
        reto: Challenge,                     // Objeto reto que se va a editar.
        actualizarLista: () -> Unit,         // Función lambda para actualizar la lista de retos después de editar.
    ) {
        // Inflador para crear la vista del diálogo a partir del layout 'DialogUpdateChallengeBinding'.
        val inflador = LayoutInflater.from(context)
        val bindingDialogo = DialogUpdateChallengeBinding.inflate(inflador)

        // Creación del cuadro de diálogo de alerta y configuración para que no sea cancelable al tocar fuera de él.
        val dialogoAlerta = AlertDialog.Builder(context).create()
        dialogoAlerta.setCancelable(false)
        dialogoAlerta.setView(bindingDialogo.root)

        // Habilita el botón 'Guardar' solo cuando el campo de texto no esté vacío.
        bindingDialogo.etContent.addTextChangedListener {
            bindingDialogo.btnGuardar.isEnabled = bindingDialogo.etContent.text.toString().isNotEmpty()
        }

        // Establece el texto actual de la descripción del reto en el campo de entrada.
        bindingDialogo.etContent.setText(reto.descripcion)

        bindingDialogo.btnCancelar.setOnClickListener {
            dialogoAlerta.dismiss()
        }

        // Configura el botón 'Guardar' para guardar los cambios y cerrar el diálogo.
        bindingDialogo.btnGuardar.setOnClickListener {
            // Obtiene el texto actualizado del campo de entrada.
            val descripcionActualizada = bindingDialogo.etContent.text.toString().trim()
            // Crea un nuevo objeto reto con el mismo ID y la descripción actualizada.
            val retoActualizado = Challenge(reto.id,descripcionActualizada)

            // Actualiza el reto utilizando el ViewModel y cierra el diálogo.
            challengeViewModel.updateChallenge(retoActualizado)
            dialogoAlerta.dismiss()

            // Llama a la función lambda para actualizar la lista de retos.
            actualizarLista.invoke()
        }

        // Muestra el cuadro de diálogo.
        dialogoAlerta.show()
    }

}