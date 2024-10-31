package com.example.dadm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Challenge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var descripcion: String,
)
