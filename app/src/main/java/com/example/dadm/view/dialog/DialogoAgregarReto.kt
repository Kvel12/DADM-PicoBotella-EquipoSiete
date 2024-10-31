package com.example.dadm.view.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.dadm.databinding.DialogAddChallengeBinding
import com.example.dadm.model.Challenge
import com.example.dadm.viewmodel.ChallengeViewModel

object DialogoAgregarReto {
    // Función para mostrar un diálogo que permite agregar un nuevo reto.
    fun mostrarDialogoAgregarReto(
        context: Context,                    // Contexto de la actividad o fragmento.
        challengeViewModel: ChallengeViewModel, // ViewModel que maneja la lógica y los datos del reto.
        actualizarLista: () -> Unit,            // Función lambda que actualiza la lista de retos después de agregar uno nuevo.
    ) {
        // Inflador para crear una vista de diálogo a partir del layout 'DialogAddChallengeBinding'.
        val inflador = LayoutInflater.from(context)
        val bindingDialogo = DialogAddChallengeBinding.inflate(inflador)

        // Creación del cuadro de diálogo de alerta y configuración para que no sea cancelable al tocar fuera de él.
        val dialogoAlerta = AlertDialog.Builder(context).create()
        dialogoAlerta.setCancelable(false)
        dialogoAlerta.setView(bindingDialogo.root)

        // Habilita el botón 'Guardar' solo cuando el campo de texto no esté vacío.
        bindingDialogo.etContent.addTextChangedListener {
            bindingDialogo.btnGuardar.isEnabled = bindingDialogo.etContent.text.toString().isNotEmpty()
        }

        // Configura el botón 'Cancelar' para cerrar el diálogo sin guardar.
        bindingDialogo.btnCancelar.setOnClickListener {
            dialogoAlerta.dismiss()
        }

        // Configura el botón 'Guardar' para almacenar el nuevo reto.
        bindingDialogo.btnGuardar.setOnClickListener {
            // Obtiene el texto del campo de entrada y crea un nuevo objeto 'Challenge' con la descripción.
            val descripcionReto = bindingDialogo.etContent.text.toString().trim()
            val nuevoReto = Challenge(descripcion = descripcionReto)

            // Guarda el nuevo reto utilizando el ViewModel y cierra el diálogo.
            challengeViewModel.saveChallenge(nuevoReto)
            dialogoAlerta.dismiss()

            // Llama a la función lambda para actualizar la lista de retos.
            actualizarLista.invoke()
        }

        // Muestra el cuadro de diálogo.
        dialogoAlerta.show()
    }

}