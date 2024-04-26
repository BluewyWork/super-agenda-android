package com.example.superagenda.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.superagenda.data.database.dao.TaskDao
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.entities.TaskEntity
import com.example.superagenda.data.database.entities.TokenEntity

@Database(
   entities = [TokenEntity::class, TaskEntity::class],
   version = 1
)
abstract class SuperAgendaDatabase : RoomDatabase()
{
   abstract fun getTokenDao(): TokenDao
   abstract fun getTaskDao(): TaskDao
}
