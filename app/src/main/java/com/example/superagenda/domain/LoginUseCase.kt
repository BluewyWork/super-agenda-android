package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.domain.models.UserForLogin
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend fun login(userForLogin: UserForLogin): Boolean {
        val token = loginRepository.retrieveTokenFromApi(userForLogin)

        if (token.isNullOrBlank()) {
            return false
        }

        loginRepository.wipeAllTokensFromLocalStorage()
        val tokenInserted = loginRepository.saveTokenToLocalStorage(token)

        return tokenInserted
    }

    suspend fun isLoggedIn(): Boolean {
        val haveToken = loginRepository.retrieveTokenFromLocalStorage()

        return !haveToken.isNullOrBlank()
    }

    suspend fun logout(): Boolean {
        return loginRepository.wipeAllTokensFromLocalStorage()
    }
}