package com.portafoliowebmariano.picobotella.view.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.dadm.viewmodel.ChallengeViewModel
import com.example.dadm.databinding.DialogAddChallengeBinding
import com.example.dadm.model.Challenge

object DialogAddChallenge {
    fun showDialogAddChallenge(
        context: Context,
        challengeViewModel: ChallengeViewModel,
        updateList: () -> Unit,
    ) {
        val inflater = LayoutInflater.from(context)
        val binding = DialogAddChallengeBinding.inflate(inflater)
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        binding.etDialogAddChallenge.addTextChangedListener {
            binding.btnGuardar.isEnabled = binding.etDialogAddChallenge.text.toString().isNotEmpty()
        }

        binding.btnCancelar.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.btnGuardar.setOnClickListener {
            val descriptionChallenge = binding.etDialogAddChallenge.text.toString().trim()
            val challenge = Challenge(description = descriptionChallenge)

            challengeViewModel.addChallenge(challenge)
            alertDialog.dismiss()
            updateList.invoke()
        }

        alertDialog.show()
    }
}