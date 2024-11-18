package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.data.SelfRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.util.AppResult
import javax.inject.Inject
import com.example.superagenda.util.Result

class UserUseCase @Inject constructor(
   private val selfRepository: SelfRepository,
   private val authenticationRepository: AuthenticationRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun retrieveUserForProfile(): UserForProfile? {
      val token = authenticationRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return null
      }

      return selfRepository.retrieveProfileFromAPI(token)
   }

   suspend fun deleteProfile(): Boolean {
      val token = authenticationRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      val isProfileDeletedAtAPI = selfRepository.deleteProfileFromApi(token)

      return isProfileDeletedAtAPI
   }

   suspend fun d(userForProfile: UserForProfile): AppResult<Unit> {
      selfRepository.upsertProfileAtDatabase(userForProfile)

      return Result.Success(Unit)
   }
}