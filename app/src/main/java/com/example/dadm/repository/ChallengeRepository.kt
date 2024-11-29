package com.example.dadm.repository

import com.example.dadm.firebase.FirebaseProvider
import com.example.dadm.model.Challenge
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChallengeRepository @Inject constructor(
    private val firebaseProvider: FirebaseProvider
) {
    private val db by lazy { firebaseProvider.getFirestore() }
    private val challengesCollection by lazy { db.collection(CHALLENGES_COLLECTION) }

    suspend fun saveChallenge(challenge: Challenge): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val newDoc = challengesCollection.document()
                val challengeWithId = challenge.copy(
                    id = newDoc.id,
                    timestamp = System.currentTimeMillis()
                )
                newDoc.set(challengeWithId.toMap()).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getListChallenge(): Result<MutableList<Challenge>> {
        return withContext(Dispatchers.IO) {
            try {
                val snapshot = challengesCollection
                    .orderBy(TIMESTAMP_FIELD, Query.Direction.DESCENDING)
                    .get()
                    .await()

                val challenges = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data
                    if (data != null) {
                        Challenge(
                            id = doc.id,
                            descripcion = data[DESCRIPTION_FIELD] as? String ?: "",
                            timestamp = data[TIMESTAMP_FIELD] as? Long ?: System.currentTimeMillis()
                        )
                    } else null
                }.toMutableList()

                Result.success(challenges)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteChallenge(challenge: Challenge): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                challengesCollection.document(challenge.id).delete().await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateChallenge(challenge: Challenge): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val updatedChallenge = challenge.copy(timestamp = System.currentTimeMillis())
                challengesCollection.document(challenge.id)
                    .set(updatedChallenge.toMap())
                    .await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    companion object {
        private const val CHALLENGES_COLLECTION = "challenges"
        private const val DESCRIPTION_FIELD = "descripcion"
        private const val TIMESTAMP_FIELD = "timestamp"
    }
}