package com.example.superagenda.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.superagenda.data.database.dao.UserForProfileDao
import com.example.superagenda.data.database.dao.TaskDao
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TaskEntity
import com.example.superagenda.data.database.entities.TokenEntity

@Database(
   entities = [TokenEntity::class, TaskEntity::class],
   version = 1
)
abstract class AppDatabase : RoomDatabase() {
   abstract fun tokenDao(): TokenDao
   abstract fun taskDao(): TaskDao
   abstract fun userForProfileDao(): UserForProfileDao
}
