package com.example.dadm.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dadm.databinding.FragmentAddItemBinding
import com.example.dadm.model.Challenge
import com.example.dadm.viewmodel.ChallengeViewModel

class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private val challengeViewModel: ChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observerViewModel()


    }

    private fun controladores() {
        validarDatos()
        binding.btnSaveInventory.setOnClickListener {
            saveChallenge()
        }
    }

    private fun saveChallenge(){
        val name = binding.etName.text.toString()
        val price = binding.etPrice.text.toString()
        val quantity = binding.etQuantity.text.toString()
        val challenge = Challenge(description = name, description = price, description = quantity)
        challengeViewModel.saveInventory(challenge)
        Log.d("test",challenge.toString())
        Toast.makeText(context,"Artículo guardado !!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()

    }

    private fun validarDatos() {
        val listEditText = listOf(binding.etName, binding.etPrice, binding.etQuantity)

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all{
                    it.text.isNotEmpty() // si toda la lista no está vacía
                }
                binding.btnSaveInventory.isEnabled = isListFull
            }
        }
    }



    private fun observerViewModel(){
        observerListProduct()
    }

    private fun observerListProduct() {

        challengeViewModel.getProducts()
        challengeViewModel.listProducts.observe(viewLifecycleOwner){ lista ->

            val product = lista[2]
            Glide.with(binding.root.context).load(product.id).into(binding.ivImagenApi)
            binding.tvTitleProduct.text = product.title
        }
    }


}