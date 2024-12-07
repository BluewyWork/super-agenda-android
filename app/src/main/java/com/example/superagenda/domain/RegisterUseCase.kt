package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.domain.models.UserForRegister
import com.example.superagenda.util.AppResult
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
   private val authenticationRepository: AuthenticationRepository,
) {

}