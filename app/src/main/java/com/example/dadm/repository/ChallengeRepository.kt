package com.example.dadm.repository
import android.content.Context
import com.example.dadm.data.ChallengeDB
import com.example.dadm.data.ChallengeDao
import com.example.dadm.model.Challenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChallengeRepository @Inject constructor(
    private val challengeDao:ChallengeDao
){

    suspend fun saveChallenge(challenge : Challenge){
        withContext(Dispatchers.IO){
            challengeDao.saveChallenge(challenge)
        }
    }

    suspend fun getListChallenge():MutableList<Challenge>{
        return withContext(Dispatchers.IO){
            challengeDao.getListChallenge()
        }
    }

    suspend fun deleteChallenge(challenge: Challenge){
        withContext(Dispatchers.IO){
            challengeDao.deleteChallenge(challenge)
        }
    }

    suspend fun updateChallenge(challenge: Challenge){
        withContext(Dispatchers.IO){
            challengeDao.updateChallenge(challenge)
        }
    }

}