package com.example.kompasandroidassessment.data.remote.response.repo

import com.example.kompasandroidassessment.data.remote.response.user.UserResponse
import com.google.gson.annotations.SerializedName

data class RepoResponse(

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("stargazers_count")
    val stargazersCount: Int,

    @field:SerializedName("visibility")
    val visibility: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("language")
    val language: String,

    @field:SerializedName("owner")
    val owner: UserResponse,

    @field:SerializedName("id")
    val id: Int
)