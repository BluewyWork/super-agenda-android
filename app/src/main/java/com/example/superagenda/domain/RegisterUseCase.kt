package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.domain.models.UserForRegister
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
   private val authenticationRepository: AuthenticationRepository,
) {

   suspend fun register(userForRegister: UserForRegister): Boolean {
      return authenticationRepository.registerAtAPI(userForRegister)
   }
}