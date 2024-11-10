package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.domain.models.UserForLogin
import javax.inject.Inject

class LoginUseCase @Inject constructor(
   private val authenticationRepository: AuthenticationRepository,
) {
   suspend fun login(userForLogin: UserForLogin): Boolean {
      val token = authenticationRepository.retrieveTokenFromAPI(userForLogin)

      if (token.isNullOrBlank()) {
         return false
      }

      authenticationRepository.clearTokensFromLocalStorage()
      val tokenInserted = authenticationRepository.insertTokenToLocalDatabase(token)

      return tokenInserted
   }

   suspend fun isLoggedIn(): Boolean {
      val haveToken = authenticationRepository.retrieveTokenFromLocalStorage()

      return !haveToken.isNullOrBlank()
   }

   suspend fun clearTokensAtLocalStorage(): Boolean {
      return authenticationRepository.clearTokensFromLocalStorage()
   }
}