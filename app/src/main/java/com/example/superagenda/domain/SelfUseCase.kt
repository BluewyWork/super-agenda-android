package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.data.SelfRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.UserForProfile
import javax.inject.Inject

class SelfUseCase @Inject constructor(
   private val selfRepository: SelfRepository,
   private val loginRepository: LoginRepository,
   private val taskRepository: TaskRepository
) {
   suspend fun retrieveUserForProfile(): UserForProfile? {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return null
      }

      return selfRepository.retrieveProfileFromAPI(token)
   }

   suspend fun deleteProfile(): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      val isProfileDeletedAtAPI = selfRepository.deleteProfileFromApi(token)

      return isProfileDeletedAtAPI
   }
}