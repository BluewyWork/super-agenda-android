package com.example.superagenda.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superagenda.data.database.entities.TokenEntity

@Dao
interface TokenDao {
   @Query("SELECT * FROM token_table")
   fun retrieveToken(): TokenEntity

   @Query("DELETE FROM token_table")
   fun nukeToken()

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   fun insertToken(tokenEntity: TokenEntity)
}
