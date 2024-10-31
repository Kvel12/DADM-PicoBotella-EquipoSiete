package com.example.dadm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dadm.databinding.ItemChallengeBinding
import com.example.dadm.model.Challenge
import com.example.dadm.view.viewholder.ChallengeViewHolder
import com.example.dadm.viewmodel.ChallengeViewModel

class ChallengeAdapter(
    private val listaRetos: MutableList<Challenge>, // Lista mutable de objetos de tipo Challenge
    private val modeloVistaReto: ChallengeViewModel, // ViewModel para la navegación y manejo de datos
):RecyclerView.Adapter<ChallengeViewHolder>() {

    // Método que crea e inicializa el ViewHolder al inflar el diseño XML de cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        // Infla el diseño del elemento de la lista usando el binding correspondiente
        val enlaceVista = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChallengeViewHolder(enlaceVista, modeloVistaReto) // Retorna el ViewHolder creado
    }

    // Retorna el tamaño de la lista de retos, necesario para el RecyclerView
    override fun getItemCount(): Int {
        return listaRetos.size
    }

    // Método que vincula los datos de cada reto con la vista correspondiente en el ViewHolder
    override fun onBindViewHolder(holder: ChallengeViewHolder, posicion: Int) {
        val reto = listaRetos[posicion] // Obtiene el reto en la posición actual
        holder.setItemChallenge(reto) // Llama al método para asignar datos a la vista
    }
}