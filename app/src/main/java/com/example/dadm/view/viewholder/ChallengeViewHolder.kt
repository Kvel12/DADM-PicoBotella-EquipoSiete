package com.example.dadm.view.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dadm.databinding.ItemChallengeBinding
import com.example.dadm.model.Challenge
import com.example.dadm.view.dialog.DialogoEditarReto.mostrarDialogoEditarReto
//import com.example.dadm.view.dialog.EliminarDialogoReto
import com.example.dadm.viewmodel.ChallengeViewModel


// Clase ChallengeViewHolder que extiende ViewHolder de RecyclerView.
// Se encarga de mantener y manipular la vista de cada elemento "reto" en el RecyclerView.
class ChallengeViewHolder(binding: ItemChallengeBinding, challengeViewModel: ChallengeViewModel) : ViewHolder(binding.root) {

    // ViewModel que maneja los datos y eventos de los retos.
    private val viewModel = challengeViewModel

    // Enlace a los elementos de la vista definidos en el layout item_challenge.xml.
    private var binding: ItemChallengeBinding

    // Bloque de inicialización que asigna el binding recibido a la variable local.
    init {
        this.binding = binding
    }

    // Función para configurar los datos y eventos de un reto específico.
    fun setItemChallenge(challenge: Challenge) {
        // Asigna la descripción del reto al TextView correspondiente en el layout.
        binding.tvDescription.text = challenge.descripcion

        // Configura el evento del botón de eliminar reto.
        //binding.ivDelete.setOnClickListener {
            // Crea y muestra un diálogo de confirmación para eliminar el reto actual.
        //    val dialog = EliminarDialogoReto(binding.root.context, viewModel, challenge)
          //  dialog.show()
        //}

        // Configura el evento del botón de editar reto.
        binding.ivEdit.setOnClickListener {
            // Muestra el diálogo para editar el reto, y actualiza la lista de retos en el ViewModel después de la edición.
            mostrarDialogoEditarReto(binding.root.context, viewModel, challenge) {
                viewModel.getListChallenge() // Refresca la lista de retos en el ViewModel.
            }
        }
    }
}
