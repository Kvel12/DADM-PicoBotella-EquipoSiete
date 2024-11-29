package com.example.dadm.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dadm.databinding.FragmentAddChallengeBinding
import com.example.dadm.view.adapter.ChallengeAdapter
import com.example.dadm.view.dialog.DialogoAgregarReto.mostrarDialogoAgregarReto
import com.example.dadm.viewmodel.ChallengeViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.dadm.utils.Result


@AndroidEntryPoint
class AgregarRetoFragment: Fragment() {
    private lateinit var vistaBinding: FragmentAddChallengeBinding
    private val vistaModeloReto: ChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vistaBinding = FragmentAddChallengeBinding.inflate(inflater)
        vistaBinding.lifecycleOwner = this
        return vistaBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurarControladores()
        observarModeloVista() // Agregar esta línea
        vistaModeloReto.getListChallenge()
    }

    private fun observarModeloVista() {
        observarListaRetos()
        observarErrores()
    }

    private fun configurarControladores() {
        vistaBinding.icContentBar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        vistaBinding.afaButton.setOnClickListener {
            mostrarDialogoAgregarReto(requireContext(), vistaModeloReto) {
                // La actualización se maneja automáticamente en el ViewModel
            }
        }
    }


    private fun observarErrores() {
        vistaModeloReto.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observarListaRetos() {
        vistaModeloReto.listChallenge.observe(viewLifecycleOwner) { resultado ->
            when (resultado) {
                is Result.Success -> {
                    vistaBinding.rvContainerChallenge.apply {
                        layoutManager = LinearLayoutManager(context).apply {
                            reverseLayout = true
                        }
                        adapter = ChallengeAdapter(resultado.data, vistaModeloReto)
                    }
                }
                is Result.Failure -> {
                    Toast.makeText(
                        context,
                        "Error al cargar los retos: ${resultado.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}