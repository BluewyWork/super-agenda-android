package com.example.superagenda.domain

import com.example.superagenda.data.RegisterRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.domain.models.UserForRegister
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val loginUseCase: LoginUseCase
) {

    suspend fun register(userForRegister: UserForRegister): Boolean {
       return registerRepository.register(userForRegister)
    }
}