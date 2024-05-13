package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.UserForLogin
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val tokenRepository: TokenRepository
) {
    suspend fun register(username: String, password: String) {

    }

    suspend fun login(userForLogin: UserForLogin): Boolean {
        val token = authenticationRepository.login(userForLogin)

        if (token.isNullOrBlank()) {
            return false
        }

        tokenRepository.wipeAllTokensFromLocalStorage()
        val tokenInserted = tokenRepository.saveTokenToLocalStorage(token)

        return tokenInserted
    }
}