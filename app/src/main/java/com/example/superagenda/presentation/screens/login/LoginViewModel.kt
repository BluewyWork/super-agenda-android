package com.example.superagenda.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
   private val loginUseCase: LoginUseCase,
) : ViewModel() {
   private val _username = MutableLiveData<String>()
   val username: LiveData<String> = _username

   private val _password = MutableLiveData<String>()
   val password: LiveData<String> = _password


   private val _errorMessage = MutableLiveData<String?>()
   val errorMessage: LiveData<String?> = _errorMessage

   fun onError(message: String) {
      _errorMessage.postValue(message)
   }

   fun onErrorDismissed() {
      _errorMessage.postValue(null)
   }

   fun onUsernameChange(username: String) {
      _username.postValue(username)
   }

   fun onPasswordChange(password: String) {
      _password.postValue(password)
   }

   fun onShow(navController: NavController) {
      viewModelScope.launch {
         val isLoggedIn = loginUseCase.isLoggedIn()

         if (!isLoggedIn) {
            return@launch
         }

         navController.navigate(Destinations.TasksNotStarted.route)
         _password.postValue("")
      }
   }

   fun onLoginButtonPress(navController: NavController) {
      viewModelScope.launch {

      }
   }
}