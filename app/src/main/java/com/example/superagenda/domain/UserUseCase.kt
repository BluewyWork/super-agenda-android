package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.data.UserRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.util.AppResult
import javax.inject.Inject
import com.example.superagenda.util.Result

class UserUseCase @Inject constructor(
   private val userRepository: UserRepository,
   private val authenticationRepository: AuthenticationRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun retrieveUserForProfile(): UserForProfile? {
      val token = authenticationRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return null
      }

      return userRepository.retrieveProfileFromAPI(token)
   }

   suspend fun deleteProfile(): Boolean {
      val token = authenticationRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      val isProfileDeletedAtAPI = userRepository.deleteProfileFromApi(token)

      return isProfileDeletedAtAPI
   }

   suspend fun upsertUserForProfileAtDatabase(userForProfile: UserForProfile): AppResult<Unit> {
      userRepository.upsertUserForProfileAtDatabase(userForProfile)

      return Result.Success(Unit)
   }

   suspend fun getUserForProfileAtDatabase(): AppResult<UserForProfile> {
     return userRepository.getUserForProfileAtDatabase()
   }
}