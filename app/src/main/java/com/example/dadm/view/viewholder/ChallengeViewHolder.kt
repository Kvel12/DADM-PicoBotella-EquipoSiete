package com.example.dadm.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dadm.R
import com.example.dadm.databinding.ItemInventoryBinding
import com.example.dadm.model.Challenge

class ChallengeViewHolder(binding: ItemInventoryBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root) {
    val bindingItem = binding
    val navController = navController
    fun setItemInventory(challenge: Challenge) {
        bindingItem.tvName.text = challenge.description
        bindingItem.tvPrice.text = "$ ${challenge.description}"
        bindingItem.tvQuantity.text = "${challenge.description}"

        bindingItem.cvInventory.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("clave", challenge)
            navController.navigate(R.id.action_homeInventoryFragment_to_itemDetailsFragment, bundle)
        }

    }
}