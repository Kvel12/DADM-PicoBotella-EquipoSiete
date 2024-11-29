package com.example.dadm.di

import com.example.dadm.firebase.DefaultFirebaseProvider
import com.example.dadm.firebase.FirebaseProvider
import com.example.dadm.repository.ChallengeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideFirebaseProvider(): FirebaseProvider {
        return DefaultFirebaseProvider()
    }

    @Provides
    @Singleton
    fun provideChallengeRepository(firebaseProvider: FirebaseProvider): ChallengeRepository {
        return ChallengeRepository(firebaseProvider)
    }
}