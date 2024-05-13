package com.example.superagenda.data

import com.example.superagenda.data.models.UserForLoginModel
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.AuthenticationService
import com.example.superagenda.domain.models.UserForLogin
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend fun register() {}

    suspend fun login(userForLogin: UserForLogin): String? {
        val userForLoginModel: UserForLoginModel = userForLogin.toDomain()

        return authenticationService.login(userForLoginModel)
    }
}