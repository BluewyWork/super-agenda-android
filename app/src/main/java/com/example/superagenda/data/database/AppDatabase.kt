package com.example.superagenda.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.superagenda.data.database.dao.MiscDao
import com.example.superagenda.data.database.dao.TaskDao
import com.example.superagenda.data.database.dao.TheRestDao
import com.example.superagenda.data.database.dao.TokenDao
import com.example.superagenda.data.database.dao.UserForProfileDao
import com.example.superagenda.data.database.entities.MiscEntity
import com.example.superagenda.data.database.entities.TaskEntity
import com.example.superagenda.data.database.entities.TheRestEntity
import com.example.superagenda.data.database.entities.TokenEntity
import com.example.superagenda.data.database.entities.UserForProfileEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(
   entities = [TokenEntity::class, TaskEntity::class, UserForProfileEntity::class, TheRestEntity::class, MiscEntity::class],
   version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
   abstract fun tokenDao(): TokenDao
   abstract fun taskDao(): TaskDao
   abstract fun userForProfileDao(): UserForProfileDao
   abstract fun theRestDao(): TheRestDao
   abstract fun miscDao(): MiscDao
}

class Converters {
   @TypeConverter
   fun fromStringList(value: List<String>?): String {
      return Gson().toJson(value)
   }

   @TypeConverter
   fun toStringList(value: String): List<String> {
      val listType = object : TypeToken<List<String>>() {}.type
      return Gson().fromJson(value, listType)
   }
}

