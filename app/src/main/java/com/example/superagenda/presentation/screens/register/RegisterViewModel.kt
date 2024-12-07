package com.example.superagenda.presentation.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.RegisterUseCase
import com.example.superagenda.domain.models.UserForRegister
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
   private val authenticationUseCase: AuthenticationUseCase
) : ViewModel() {
   private val _username = MutableStateFlow<String>("")
   val username: StateFlow<String> = _username

   private val _password = MutableStateFlow<String>("")
   val password: StateFlow<String> = _password

   private val _passwordConfirm = MutableStateFlow("")
   val passwordConfirm: StateFlow<String> = _passwordConfirm

   private val _popupsQueue = MutableStateFlow<List<Triple<String, String, String>>>(emptyList())
   val popupsQueue: StateFlow<List<Triple<String, String, String>>> = _popupsQueue

   fun onUsernameChange(username: String) {
      _username.value = username
   }

   fun onPasswordChange(password: String) {
      _password.value =password
   }

   fun enqueuePopup(title: String, message: String, error: String = "") {
      _popupsQueue.value =
         popupsQueue.value + Triple(title, message, error)
   }

   fun dismissPopup() {
      _popupsQueue.value = _popupsQueue.value.drop(1)
   }

   private suspend fun whenPopupsEmpty(code: () -> Unit) {
      while (popupsQueue.value.isNotEmpty()) {
         delay(2000)
      }

      code()
   }

   fun onPasswordConfirmChange(passwordConfirm: String) {
      _passwordConfirm.value = passwordConfirm
   }

   fun onRegisterButtonPress(navController: NavController) {
      viewModelScope.launch {
         val username = username.value
         val password = password.value
         val passwordConfirm = _passwordConfirm.value

         if (username.isBlank()) {
            enqueuePopup("ERROR", "Field username is not valid...")
            return@launch
         }

         if (password.isBlank()) {
            enqueuePopup("ERROR", "Field password is not valid...")
            return@launch
         }

         if (password != passwordConfirm) {
            enqueuePopup("ERROR", "Passwords does not match...")
            return@launch
         }

         when (val resultRegister = authenticationUseCase.register(
            UserForRegister(
               username,
               password
            )
         )) {
            is Result.Error -> {
               enqueuePopup("ERROR", "Failed to register...", resultRegister.error.toString())
            }

            is Result.Success -> {
               enqueuePopup("INFO", "Successfully registered..")

               whenPopupsEmpty {
                  navController.navigate(Destinations.Login.route)
               }
            }
         }
      }
   }
}