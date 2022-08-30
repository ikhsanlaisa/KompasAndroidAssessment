package com.example.kompasandroidassessment.data.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL: String = "https://api.github.com/"
const val GET_USERS: String = "users"

//Notes:
//Coroutine adalah pola desain serentak yang dapat Anda gunakan di Android untuk menyederhanakan kode yang dieksekusi secara asinkron.

interface ServiceApi {

    //GET LIST USERS
    @GET(GET_USERS)
    //Notes:
    //Suspend Function adalah fungsi khusus yang digunakan untuk menjalankan proses yang lama. Untuk menjalankan suspend function, ia harus di dalam Coroutine Builder.
    suspend fun getListUsers(@Query("q") searchQuery: String,
                             @Query("per_page") perPage: Int): Response<>

    //Companion Object Kotlin adalah sebuah deklarsi objek yang ada di dalam sebuah kelas
    companion object {
        operator fun invoke(): ServiceApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceApi::class.java)
        }
    }
}