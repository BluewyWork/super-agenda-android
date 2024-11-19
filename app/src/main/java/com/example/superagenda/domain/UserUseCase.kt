package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.data.UserRepository
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import javax.inject.Inject

class UserUseCase @Inject constructor(
   private val userRepository: UserRepository,
   private val authenticationRepository: AuthenticationRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun getUserForProfileAtApi(): AppResult<UserForProfile> {
      val token = when (val tokenResult = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(tokenResult.error)
         is Result.Success -> tokenResult.data
      }

      return userRepository.getUserForProfileAtApi(token)
   }

   suspend fun deleteUserAtApi(): AppResult<Boolean> {
      val token = when (val tokenResult = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(tokenResult.error)
         is Result.Success -> tokenResult.data
      }

      return userRepository.deleteUserAtApi(token)
   }

   suspend fun upsertUserForProfileAtDatabase(userForProfile: UserForProfile): AppResult<Unit> {
      userRepository.upsertUserForProfileAtDatabase(userForProfile)

      return Result.Success(Unit)
   }

   suspend fun getUserForProfileAtDatabase(): AppResult<UserForProfile> {
      return userRepository.getUserForProfileAtDatabase()
   }
}