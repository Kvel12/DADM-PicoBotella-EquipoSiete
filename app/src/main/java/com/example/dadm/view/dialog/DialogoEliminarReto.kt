package com.example.dadm.view.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.dadm.model.Challenge
import com.example.dadm.databinding.DialogDeleteChallengeBinding
import com.example.dadm.viewmodel.ChallengeViewModel


object DialogoEliminarReto {
    fun mostrarDialogoEliminarReto(
        context: Context,                    // Contexto de la actividad o fragmento.
        challengeViewModel: ChallengeViewModel, // ViewModel que maneja la lógica y los datos del reto.
        reto: Challenge,    // reto que se va a editar
        actualizarLista: () -> Unit,       // Función lambda que actualiza la lista de retos después de agregar uno nuevo.
    ){
        // Inflador para crear la vista del diálogo a partir del layout 'DialogUpdateChallengeBinding'.
        val inflador = LayoutInflater.from(context)
        val bindingDialogo = DialogDeleteChallengeBinding.inflate(inflador)

        // Creación del cuadro de diálogo de alerta y configuración para que no sea cancelable al tocar fuera de él.
        val dialogoAlerta = AlertDialog.Builder(context).create()
        dialogoAlerta.setCancelable(false)
        dialogoAlerta.setView(bindingDialogo.root)

        // Establece el texto actual de la descripción del reto en el campo de entrada.
        bindingDialogo.etContent.setText(reto.descripcion)

        // Configura el botón 'Cancelar' para cerrar el diálogo sin guardar.
        bindingDialogo.cancelButton.setOnClickListener {
            dialogoAlerta.dismiss()
        }

        bindingDialogo.okButton.setOnClickListener{
            val descripcionReto = bindingDialogo.etContent.text.toString().trim()
            val eliminar = Challenge(reto.id,descripcionReto)
            challengeViewModel.deleteChallenge(eliminar)
            dialogoAlerta.dismiss()

            //se actualiza la lista de retos
            actualizarLista.invoke()

            //muestra el cuadro de dialogo
            dialogoAlerta.show()

        }




    }
}


