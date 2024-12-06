package com.example.superagenda.domain

import com.example.superagenda.data.LastModifiedRepository
import com.example.superagenda.util.AppResult
import java.time.LocalDateTime
import javax.inject.Inject

class LastModifiedUseCase @Inject constructor(
   private val lastModifiedRepository: LastModifiedRepository,
) {
   suspend fun getLastModified(): AppResult<LocalDateTime> {
      return lastModifiedRepository.getLastModifiedDao()
   }

   suspend fun upsertLastModified(lastModified: LocalDateTime): AppResult<Unit> {
      return lastModifiedRepository.upsertLastModifiedDao(lastModified)
   }
}