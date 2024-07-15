package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.data.SelfRepository
import com.example.superagenda.domain.models.UserForProfile
import javax.inject.Inject

class SelfUseCase @Inject constructor(
    private val selfRepository: SelfRepository,
    private val loginRepository: LoginRepository
) {
    suspend fun retrieveUserForProfile(): UserForProfile? {
        val token = loginRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return null
        }

        return selfRepository.retrieveUserProfile(token)
    }

    suspend fun deleteProfile(): Boolean {
        val token = loginRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return false
        }

        if (!selfRepository.deleteProfile(token) || !loginRepository.wipeAllTokensFromLocalStorage()) {
            return false
        }

        return true
    }
}