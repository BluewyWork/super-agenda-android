package com.example.superagenda.domain

import com.example.superagenda.data.ProfileRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.UserForProfile
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val tokenRepository: TokenRepository
) {
    suspend fun retrieveUserForProfile(): UserForProfile? {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return null
        }

        return profileRepository.retrieveUserProfile(token)
    }

    suspend fun deleteProfile(): Boolean {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return false
        }

        if (!profileRepository.deleteProfile(token) || !tokenRepository.wipeAllTokensFromLocalStorage()) {
            return false
        }

        return true
    }
}