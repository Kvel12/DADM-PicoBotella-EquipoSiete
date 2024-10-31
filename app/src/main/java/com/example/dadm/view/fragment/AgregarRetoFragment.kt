package com.example.dadm.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dadm.databinding.FragmentAddChallengeBinding
import com.example.dadm.view.adapter.ChallengeAdapter
import com.example.dadm.view.dialog.DialogoAgregarReto.mostrarDialogoAgregarReto
import com.example.dadm.viewmodel.ChallengeViewModel

class AgregarRetoFragment: Fragment() {
    // Importación necesaria para la vinculación de vistas
    private lateinit var vistaBinding: FragmentAddChallengeBinding // Vincula el diseño XML del fragmento
    private val vistaModeloReto: ChallengeViewModel by viewModels() // Instancia del ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el diseño XML del fragmento y lo enlaza con la clase de vinculación
        vistaBinding = FragmentAddChallengeBinding.inflate(inflater)
        vistaBinding.lifecycleOwner = this // Define el ciclo de vida del propietario
        return vistaBinding.root // Retorna la raíz de la vista vinculada
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarControladores() // Inicializa los controladores de eventos
        observarModeloVista() // Observa los cambios en el ViewModel
    }

    private fun configurarControladores(){
        vistaBinding.icContentBar.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

        vistaBinding.afaButton.setOnClickListener {
            mostrarDialogoAgregarReto(requireContext(), vistaModeloReto) {
                vistaModeloReto.getListChallenge() // Actualiza la lista después de agregar un reto
            }
        }
    }

    private fun observarModeloVista() {
        observarListaRetos() // Llama al método para observar los cambios en la lista de retos
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observarListaRetos() {
        vistaModeloReto.getListChallenge() // Solicita la lista de retos desde el ViewModel
        vistaModeloReto.listChallenge.observe(viewLifecycleOwner) { listaRetos ->

            // Configura el RecyclerView para mostrar la lista de retos
            val recycler = vistaBinding.rvContainerChallenge
            val layoutManager = LinearLayoutManager(context)
            layoutManager.reverseLayout = true // Invierte el orden de la lista
            recycler.layoutManager = layoutManager
            val adaptador = ChallengeAdapter(listaRetos, vistaModeloReto)
            recycler.adapter = adaptador
            adaptador.notifyDataSetChanged() // Notifica cambios en el adaptador
        }
    }





}