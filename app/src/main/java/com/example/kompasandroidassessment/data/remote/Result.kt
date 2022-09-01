package com.example.kompasandroidassessment.data.remote

sealed class Result<out R> {
    data class Success<out T>(val data: T?): Result<T>()
    data class Error(val message: String): Result<Nothing>()
}