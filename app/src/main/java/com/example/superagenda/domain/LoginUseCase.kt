package com.example.superagenda.domain

import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.UserForLogin
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend fun login(userForLogin: UserForLogin): Boolean {
        val token = tokenRepository.retrieveTokenFromApi(userForLogin)

        if (token.isNullOrBlank()) {
            return false
        }

        tokenRepository.wipeAllTokensFromLocalStorage()
        val tokenInserted = tokenRepository.saveTokenToLocalStorage(token)

        return tokenInserted
    }

    suspend fun isLoggedIn(): Boolean {
        val haveToken = tokenRepository.retrieveTokenFromLocalStorage()

        return !haveToken.isNullOrBlank()
    }

    suspend fun logout():Boolean {
       return tokenRepository.wipeAllTokensFromLocalStorage()
    }
}