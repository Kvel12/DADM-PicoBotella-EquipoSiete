package com.example.dadm.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dadm.R
import com.example.dadm.databinding.FragmentHomeInventoryBinding
import com.example.dadm.view.adapter.ChallengeAdapter
import com.example.dadm.viewmodel.ChallengeViewModel
class HomeInventoryFragment : Fragment() {
    private lateinit var binding: FragmentHomeInventoryBinding
    private val challengeViewModel: ChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeInventoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores()
        observadorViewModel()

    }

    private fun controladores() {
        binding.fbagregar.setOnClickListener {
            findNavController().navigate(R.id.action_homeInventoryFragment_to_addItemFragment)
        }

    }

    private fun observadorViewModel(){
//        observerListInventory()
        observerProgress()
    }

//    private fun observerListInventory(){
//
//        challengeViewModel.getListInventory()
//        challengeViewModel.listChallenge.observe(viewLifecycleOwner){ listInventory ->
//            val recycler = binding.recyclerview
//            val layoutManager =LinearLayoutManager(context)
//            recycler.layoutManager = layoutManager
//            val adapter = ChallengeAdapter(listInventory, findNavController())
//            recycler.adapter = adapter
//            adapter.notifyDataSetChanged()
//
//        }
//
//    }
    private fun observerProgress(){
        challengeViewModel.progresState.observe(viewLifecycleOwner){ status ->
            binding.progress.isVisible = status
        }
    }

}