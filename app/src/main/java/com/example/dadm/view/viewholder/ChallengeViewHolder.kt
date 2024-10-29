package com.example.dadm.view.viewholder

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dadm.R
import com.example.dadm.databinding.ItemInventoryBinding
import com.example.dadm.model.Challenge
import com.example.dadm.viewmodel.ChallengeViewModel

class ChallengeViewHolder(binding: ItemInventoryBinding, challengeViewModel: ChallengeViewModel) :
    ViewHolder(binding.root) {
    private var bindingItem = binding
    private val viewModel = challengeViewModel
    init {
        this.bindingItem = binding
    }

}