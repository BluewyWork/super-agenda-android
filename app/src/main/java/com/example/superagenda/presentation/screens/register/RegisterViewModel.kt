package com.example.superagenda.presentation.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.RegisterUseCase
import com.example.superagenda.domain.models.UserForRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

   private val _popupsQueue = MutableLiveData<List<Pair<String, String>>>()
   val popupsQueue: LiveData<List<Pair<String, String>>> = _popupsQueue

   fun onUsernameChange(username: String) {
      _username.postValue(username)
   }

   fun onPasswordChange(password: String) {
      _password.postValue(password)
   }

   fun enqueuePopup(title: String, message: String) {
      _popupsQueue.value =
         popupsQueue.value?.plus(Pair(title, message)) ?: listOf(
            Pair(
               title,
               message
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

   fun onRegisterButtonPress(navController: NavController) {
      viewModelScope.launch {
         val username = username.value
         if (username.isNullOrBlank()) {
            enqueuePopup("ERROR", "Field username is not valid...")
            return@launch
         }

         val password = password.value
         if (password.isNullOrBlank()) {
            enqueuePopup("ERROR", "Field password is not valid...")
            return@launch
         }

         val ok = registerUseCase.register(
            UserForRegister(
               username,
               password
            )
         )

         if (ok) {
            _password.postValue("")
            enqueuePopup("INFO", "Successfully registered...")

            whenPopupsEmpty { navController.navigateUp() }
         } else {
            enqueuePopup("ERROR", "Something went wrong...")
         }
      }
   }
}