package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.superagenda.data.database.entities.TokenEntity

@Dao
interface TokenDao {
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun upsert(tokenEntity: TokenEntity)

   @Query("SELECT * FROM token_table LIMIT 1")
   fun get(): TokenEntity

   @Query("DELETE FROM token_table")
   fun delete()
}
