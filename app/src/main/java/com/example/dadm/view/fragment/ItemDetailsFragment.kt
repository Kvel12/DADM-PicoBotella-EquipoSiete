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
//import com.example.dadm.databinding.FragmentItemDetailsBinding
//import com.example.dadm.model.Challenge
//import com.example.dadm.viewmodel.ChallengeViewModel
//
//class ItemDetailsFragment : Fragment() {
//    private lateinit var binding: FragmentItemDetailsBinding
//    private val challengeViewModel: ChallengeViewModel by viewModels()
//    private lateinit var receivedChallenge: Challenge
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentItemDetailsBinding.inflate(inflater)
//        binding.lifecycleOwner = this
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        dataInventory()
//        controladores()
//    }
//
//    private fun controladores() {
//        binding.btnDelete.setOnClickListener {
//            deleteInventory()
//        }
//
//        binding.fbEdit.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putSerializable("dataInventory", receivedChallenge)
//            findNavController().navigate(R.id.action_itemDetailsFragment_to_itemEditFragment, bundle)
//        }
//    }
//
//    private fun dataInventory() {
//        val receivedBundle = arguments
//        receivedChallenge = receivedBundle?.getSerializable("clave") as Challenge
//        binding.tvItem.text = "${receivedChallenge.description}"
//        binding.tvPrice.text = "$ ${receivedChallenge.description}"
//        binding.tvQuantity.text = "${receivedChallenge.description}"
//////        binding.txtTotal.text = "$ ${
////////            challengeViewModel.totalProducto(
////////                receivedChallenge.description,
////////                receivedChallenge.description
//////            )
////        }"
//    }
//
//    private fun deleteInventory(){
//        challengeViewModel.deleteInventory(receivedChallenge)
//        challengeViewModel.getListInventory()
//        findNavController().popBackStack()
//    }
//
//}