package com.example.kompasandroidassessment.di

import com.example.kompasandroidassessment.data.local.LocalDataSource
import com.example.kompasandroidassessment.data.local.room.UserDatabase
import com.example.kompasandroidassessment.data.remote.RemoteDataSource
import com.example.kompasandroidassessment.data.remote.network.ServiceApi
import com.example.kompasandroidassessment.data.repository.UserRepository
import com.example.kompasandroidassessment.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): UserRepository =
        UserRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ServiceApi): RemoteDataSource =
        RemoteDataSource(apiService)
}