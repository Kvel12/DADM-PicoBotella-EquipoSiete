package com.example.dadm.di

import android.content.Context
import androidx.room.Room
import com.example.dadm.data.ChallengeDB
import com.example.dadm.data.ChallengeDao
import com.example.dadm.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideChallengeDB(@ApplicationContext context: Context): ChallengeDB {
        return Room.databaseBuilder(
            context,
            ChallengeDB::class.java,
            Constants.NAME_BD
            ).build()
    }

//    @Provides
//    fun provideChallengeDao(challengeDB: ChallengeDB): ChallengeDao {
//        return challengeDB.getChallengeDao()
//    }

//    @Provides
//    @Singleton
//    fun provideChallengeRepository(challengeDao: ChallengeDao): ChallengeRepository {
//        return ChallengeRepository(challengeDao)
//    }

    @Provides
    @Singleton
    fun provideDaoChallenge(challengeDB: ChallengeDB): ChallengeDao {
        return challengeDB.challengeDao()
    }
}