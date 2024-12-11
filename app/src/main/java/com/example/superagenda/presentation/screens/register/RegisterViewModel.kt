package com.example.superagenda.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.models.UserForRegister
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
   private val authenticationUseCase: AuthenticationUseCase,
) : ViewModel() {
   private val _username = MutableStateFlow("")
   val username: StateFlow<String> = _username

   private val _password = MutableStateFlow("")
   val password: StateFlow<String> = _password

   private val _passwordConfirm = MutableStateFlow("")
   val passwordConfirm: StateFlow<String> = _passwordConfirm

   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popups: StateFlow<List<Popup>> = _popups

   fun onUsernameChanged(username: String) {
      _username.value = username
   }

   fun onPasswordChanged(password: String) {
      _password.value = password
   }

   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   fun onPasswordConfirmChanged(passwordConfirm: String) {
      _passwordConfirm.value = passwordConfirm
   }

   fun onRegisterButtonPressed(navController: NavController) {
      viewModelScope.launch {
         val username = username.value
         val password = password.value
         val passwordConfirm = _passwordConfirm.value

         if (username.isBlank()) {
            _popups.value += Popup("ERROR", "Field username is not valid...")
            return@launch
         }

         if (password.isBlank()) {
            _popups.value += Popup("ERROR", "Field password is not valid...")
            return@launch
         }

         if (password != passwordConfirm) {
            _popups.value += Popup("ERROR", "Passwords does not match...")
            return@launch
         }

         when (val resultRegister = authenticationUseCase.register(
            UserForRegister(
               username,
               password
            )
         )) {
            is Result.Error -> {
               _popups.value += Popup(
                  "ERROR",
                  "Failed to register...",
                  resultRegister.error.toString()
               )
            }

            is Result.Success -> {
               _popups.value += Popup("INFO", "Successfully registered..") {
                  navController.navigate(Destinations.Login.route)
               }
            }
         }
      }
   }
}

data class Popup(
   val title: String = "",
   val description: String = "",
   val error: String = "",
   val code: () -> Unit = {},
)
