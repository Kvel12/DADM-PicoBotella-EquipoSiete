package com.example.dadm.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dadm.model.Challenge
import javax.inject.Inject

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChallenge(challenge: Challenge)

    @Query("SELECT * FROM Challenge")
    suspend fun getListChallenge(): MutableList<Challenge>

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Update
    suspend fun updateChallenge(challenge: Challenge)
}