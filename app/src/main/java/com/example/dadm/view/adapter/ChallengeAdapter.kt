package com.example.dadm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.dadm.databinding.ItemInventoryBinding
import com.example.dadm.model.Challenge
import com.example.dadm.view.viewholder.ChallengeViewHolder

class ChallengeAdapter(private val listChallenge:MutableList<Challenge>, private val navController: NavController):RecyclerView.Adapter<ChallengeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemInventoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ChallengeViewHolder(binding, navController)
    }

    override fun getItemCount(): Int {
        return listChallenge.size
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val inventory = listChallenge[position]
        holder.setItemInventory(inventory)
    }
}