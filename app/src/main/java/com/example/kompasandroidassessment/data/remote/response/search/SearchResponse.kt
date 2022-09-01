package com.example.kompasandroidassessment.data.remote.response.search

import com.example.kompasandroidassessment.data.remote.response.user.UserResponse
import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @field:SerializedName("items")
    val items: List<UserResponse>
)