package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.data.TheRestRepository
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import java.time.LocalDateTime
import javax.inject.Inject

class TheRestUseCase @Inject constructor(
   private val authenticationRepository: AuthenticationRepository,
   private val theRestRepository: TheRestRepository,
) {
   suspend fun getLastModifiedAtDatabase(): AppResult<LocalDateTime> {
      return theRestRepository.getLastModifiedAtDatabase()
   }

   suspend fun upsertLastModifiedAtDatabase(lastModified: LocalDateTime): AppResult<Unit> {
      return theRestRepository.upsertLastModifiedAtDatabase(lastModified)
   }

   suspend fun getLastModifiedAtApi(): AppResult<LocalDateTime?> {
      val token = when (val result = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(result.error)
         is Result.Success -> result.data
      }

      return theRestRepository.getLastModifiedAtApi(token)
   }

   suspend fun updateLastModifiedAtApi(lastModified: LocalDateTime): AppResult<Unit> {
      val token = when (val result = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(result.error)
         is Result.Success -> result.data
      }

      return theRestRepository.updateLastModifiedAtApi(token, lastModified)
   }
}