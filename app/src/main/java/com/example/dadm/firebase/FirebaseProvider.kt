package com.example.dadm.firebase
import com.google.firebase.firestore.FirebaseFirestore

interface FirebaseProvider {
    fun getFirestore(): FirebaseFirestore
}