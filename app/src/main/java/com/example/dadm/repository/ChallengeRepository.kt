package com.example.dadm.repository
import android.content.Context
import com.example.dadm.data.ChallengeDB
import com.example.dadm.data.ChallengeDao
import com.example.dadm.model.Challenge
import com.example.dadm.model.ProductModelResponse
import com.example.dadm.webservice.ApiService
import com.example.dadm.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChallengeRepository(val context: Context){
    private var challengeDao:ChallengeDao = ChallengeDB.getDatabase(context).challengeDao()
    private var apiService: ApiService = ApiUtils.getApiService()

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

    suspend fun getProducts(): MutableList<ProductModelResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts()
                response
            } catch (e: Exception) {

                e.printStackTrace()
                mutableListOf()
            }
        }
    }
}