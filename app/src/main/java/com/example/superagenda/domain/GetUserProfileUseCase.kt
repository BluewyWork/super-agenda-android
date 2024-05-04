package com.example.superagenda.domain

import com.example.superagenda.domain.models.UserProfile
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor() {
    suspend operator fun invoke(): UserProfile? {
        return UserProfile("FakeUser")
    }
}