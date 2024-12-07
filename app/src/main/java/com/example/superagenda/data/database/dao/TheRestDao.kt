package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.superagenda.data.database.entities.TheRestEntity

@Dao
interface TheRestDao {
   @Upsert
   fun upsert(theRestEntity: TheRestEntity)

   @Query("SELECT * FROM the_rest_table LIMIT 1")
   fun get(): TheRestEntity

   @Query("DELETE FROM the_rest_table")
   fun delete()
}