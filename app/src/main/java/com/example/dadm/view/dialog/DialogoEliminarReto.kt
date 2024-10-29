package com.example.dadm.view.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.dadm.model.Challenge
import com.example.dadm.viewmodel.ChallengeViewModel


fun EliminarDialogoReto(context: Context, challengeViewModel: ChallengeViewModel, challenge: Challenge): AlertDialog{

    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)
    builder.setTitle("¿Deseas eliminas este reto?")
        .setMessage("\n${challenge.description}")
        .setPositiveButton("SI"){dialogo, with ->
            challengeViewModel.deleteChallenge(challenge)
            dialogo.dismiss()
            challengeViewModel.getListChallenge()
        }
        .setNegativeButton("NO"){dialogo, with ->
            dialogo.dismiss()
        }
    return builder.create()
}

