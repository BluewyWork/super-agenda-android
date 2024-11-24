package com.example.superagenda.presentation.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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
   private val registerUseCase: RegisterUseCase,
) : ViewModel() {
   private val _username = MutableLiveData<String>()
   val username: LiveData<String> = _username

   private val _password = MutableLiveData<String>()
   val password: LiveData<String> = _password

   private val _passwordConfirm = MutableStateFlow("")
   val passwordConfirm: StateFlow<String> = _passwordConfirm

   private val _popupsQueue = MutableLiveData<List<Triple<String, String, String>>>()
   val popupsQueue: LiveData<List<Triple<String, String, String>>> = _popupsQueue

   fun onUsernameChange(username: String) {
      _username.postValue(username)
   }

   fun onPasswordChange(password: String) {
      _password.postValue(password)
   }

   fun enqueuePopup(title: String, message: String, error: String = "") {
      _popupsQueue.value =
         popupsQueue.value?.plus(Triple(title, message, error)) ?: listOf(
            Triple(
               title,
               message,
               error
            )
         )
   }

   fun dismissPopup() {
      _popupsQueue.postValue(_popupsQueue.value?.drop(1))
   }

   private suspend fun whenPopupsEmpty(code: () -> Unit) {
      while (popupsQueue.value?.isNotEmpty() == true) {
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

         if (username.isNullOrBlank()) {
            enqueuePopup("ERROR", "Field username is not valid...")
            return@launch
         }

         if (password.isNullOrBlank()) {
            enqueuePopup("ERROR", "Field password is not valid...")
            return@launch
         }

         if (password != passwordConfirm) {
            enqueuePopup("ERROR", "Passwords does not match...")
            return@launch
         }

         when (val resultRegister = registerUseCase.register(
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