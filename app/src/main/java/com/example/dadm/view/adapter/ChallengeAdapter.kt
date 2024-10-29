package com.example.dadm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dadm.databinding.ItemInventoryBinding
import com.example.dadm.model.Challenge
import com.example.dadm.view.viewholder.ChallengeViewHolder
import com.example.dadm.viewmodel.ChallengeViewModel

class ChallengeAdapter(
    private val listChallenge: MutableList<Challenge>,
    private val challengeViewModel: ChallengeViewModel,
) :
    RecyclerView.Adapter<ChallengeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemInventoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ChallengeViewHolder(binding, challengeViewModel)
    }

    override fun getItemCount(): Int {
        return listChallenge.size
    }

    override fun onBindViewHolder(challengeViewHolder: ChallengeViewHolder, position: Int) {
        val inventory = listChallenge[position]
//      challengeViewHolder.setItemInventory(inventory)
    }
}