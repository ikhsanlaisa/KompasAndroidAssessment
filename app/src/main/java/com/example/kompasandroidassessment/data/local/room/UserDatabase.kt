package com.example.kompasandroidassessment.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kompasandroidassessment.data.local.entity.DetailUserEntity
import com.example.kompasandroidassessment.data.local.entity.UserEntity

@Database(entities = [DetailUserEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}