package com.example.dadm.firebase
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class DefaultFirebaseProvider @Inject constructor() : FirebaseProvider {
    override fun getFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}