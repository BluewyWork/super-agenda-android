package com.example.superagenda.domain

import com.example.superagenda.data.MiscRepository
import com.example.superagenda.util.AppResult
import javax.inject.Inject

class MiscUseCase @Inject constructor(
   private val miscRepository: MiscRepository,
) {
   suspend fun getScreenShownAtDatabase(): AppResult<Boolean> {
      return miscRepository.getScreenShownAtDatabase()
   }

   suspend fun updateScreenShownAtDatabase(shown: Boolean): AppResult<Unit> {
      return miscRepository.updateScreenShownAtDatabase(shown)
   }
}