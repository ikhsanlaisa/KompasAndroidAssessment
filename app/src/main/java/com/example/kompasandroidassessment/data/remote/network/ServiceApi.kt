package com.example.kompasandroidassessment.data.remote.network

import com.example.kompasandroidassessment.data.remote.response.detail.DetailUserResponse
import com.example.kompasandroidassessment.data.remote.response.detailrepo.DetailRepoResponse
import com.example.kompasandroidassessment.data.remote.response.repo.RepoResponse
import com.example.kompasandroidassessment.data.remote.response.search.SearchResponse
import com.example.kompasandroidassessment.data.remote.response.user.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String
    ): SearchResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String
    ): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String
    ): List<UserResponse>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String
    ): List<UserResponse>

    @GET("users/{username}/repos")
    suspend fun getRepos(
        @Path("username") username: String
    ): List<RepoResponse>

    @GET("repos/{username}/{repository}")
    suspend fun getDetailRepo(
        @Path("username") username: String,
        @Path("repository") repository: String
    ): DetailRepoResponse
}