package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.domain.models.UserForLogin
import javax.inject.Inject

class LoginUseCase @Inject constructor(
   private val loginRepository: LoginRepository,
) {
   suspend fun login(userForLogin: UserForLogin): Boolean {
      val token = loginRepository.retrieveTokenFromAPI(userForLogin)

      if (token.isNullOrBlank()) {
         return false
      }

      loginRepository.clearTokensFromLocalStorage()
      val tokenInserted = loginRepository.insertTokenToLocalDatabase(token)

      return tokenInserted
   }

   suspend fun isLoggedIn(): Boolean {
      val haveToken = loginRepository.retrieveTokenFromLocalStorage()

      return !haveToken.isNullOrBlank()
   }

   suspend fun clearTokensAtLocalStorage(): Boolean {
      return loginRepository.clearTokensFromLocalStorage()
   }
}