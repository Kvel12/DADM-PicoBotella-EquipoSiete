//package com.example.dadm.view.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.example.dadm.R
//import com.example.dadm.databinding.FragmentItemEditBinding
//import com.example.dadm.model.Challenge
//import com.example.dadm.viewmodel.ChallengeViewModel
//
//class ItemEditFragment : Fragment() {
//    private lateinit var binding: FragmentItemEditBinding
//    private val challengeViewModel: ChallengeViewModel by viewModels()
//    private lateinit var receivedChallenge: Challenge
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentItemEditBinding.inflate(inflater)
//        binding.lifecycleOwner = this
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
////        dataInventory()
////        controladores()
//
//    }
////
////    private fun controladores(){
////        binding.btnEdit.setOnClickListener {
////            updateInventory()
////        }
////    }
////
////    private fun dataInventory(){
////        val receivedBundle = arguments
////        receivedChallenge = receivedBundle?.getSerializable("dataInventory") as Challenge
////        binding.etName.setText(receivedChallenge.name)
////        binding.etPrice.setText(receivedChallenge.price.toString())
////        binding.etQuantity.setText(receivedChallenge.quantity.toString())
////
////    }
////
////    private fun updateInventory(){
////        val name = binding.etName.text.toString()
////        val price = binding.etPrice.text.toString().toInt()
////        val quantity = binding.etQuantity.text.toString().toInt()
////        val challenge = Challenge(receivedChallenge.id, name,price,quantity)
////        challengeViewModel.updateInventory(challenge)
////        findNavController().navigate(R.id.action_itemEditFragment_to_homeInventoryFragment)
//
//    }
