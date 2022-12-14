package com.example.kompasandroidassessment.data.remote

import com.example.kompasandroidassessment.data.remote.network.ServiceApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ServiceApi) {

    suspend fun getUsers() = apiService.getUsers()

    suspend fun searchUser(query: String) = apiService.searchUser(query)

    suspend fun getDetailUser(username: String) = apiService.getDetailUser(username)

    suspend fun getFollowers(username: String) = apiService.getFollowers(username)

    suspend fun getFollowing(username: String) = apiService.getFollowing(username)

    suspend fun getRepos(username: String) = apiService.getRepos(username)

    suspend fun getDetailRepo(username: String, repository: String) =
        apiService.getDetailRepo(username, repository)
}