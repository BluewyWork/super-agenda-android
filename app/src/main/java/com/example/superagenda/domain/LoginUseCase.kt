package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
   private val authenticationRepository: AuthenticationRepository,
) {
   suspend fun login(userForLogin: UserForLogin): AppResult<Unit> {
      val token = when (val result = authenticationRepository.getTokenAtApi(userForLogin)) {
         is Result.Error -> return Result.Error(result.error)
         is Result.Success -> result.data
      }

//      authenticationRepository.clearTokensFromLocalStorage()
      return authenticationRepository.upsertTokenAtDatabase(token)
   }

   suspend fun isLoggedIn(): AppResult<Unit> {
      return when (val result = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> Result.Error(result.error)
         is Result.Success -> Result.Success(Unit)
      }
   }

   suspend fun deleteTokenFromDatabase(): AppResult<Unit> {
      return authenticationRepository.deleteTokenAtDatabase()
   }
}