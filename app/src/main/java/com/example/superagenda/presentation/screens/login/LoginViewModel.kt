package com.example.superagenda.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.UserUseCase
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
   private val authenticationUseCase: AuthenticationUseCase,
   private val taskUseCase: TaskUseCase,
   private val userUseCase: UserUseCase,
) : ViewModel() {
   private val _username = MutableStateFlow("")
   val username: StateFlow<String> = _username

   private val _password = MutableStateFlow("")
   val password: StateFlow<String> = _password

   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popups: StateFlow<List<Popup>> = _popups

   fun onUsernameChanged(username: String) {
      _username.value = username
   }

   fun onPasswordChanged(password: String) {
      _password.value = (password)
   }

   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   fun onLoginButtonPress(onSuccess: () -> Unit) {
      viewModelScope.launch {
         val username = username.value
         val password = password.value

         if (username.isBlank()) {
            _popups.value += Popup("ERROR", "Field username is not valid...")
            return@launch
         }

         if (password.isBlank()) {
            _popups.value += Popup("ERROR", "Field password is not valid...")
            return@launch
         }

         when (val resultLogin = authenticationUseCase.login(
            UserForLogin(
               username,
               password
            )
         )) {
            is Result.Error -> _popups.value += Popup(
               "ERROR",
               "Failed to log in...",
               resultLogin.error.toString()
            )

            is Result.Success -> {
               _password.value = ("")
               _popups.value += Popup("INFO", "Successfully logged in!")

               when (val resultGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
                  is Result.Error ->
                     _popups.value += Popup("ERROR", "Failed to get tasks at api...")

                  is Result.Success -> {
                     val tasksDatabase = resultGetTasksAtDatabase.data
                     var lastResult = true

                     for (task in tasksDatabase) {
                        lastResult =
                           when (val resultCreateTaskAtApi = taskUseCase.createTaskAtApi(task)) {
                              is Result.Error -> {
                                 //                              _popups.value += Popup(
                                 //                                 "ERROR",
                                 //                                 "Failed to create task at api...",
                                 //                                 resultCreateTaskAtApi.error.toString()
                                 //                              )
                                 false
                              }

                              is Result.Success -> resultCreateTaskAtApi.data
                           }
                     }

                     if (!lastResult) {
                        _popups.value += Popup(
                           "ERROR",
                           "Failed to create tasks at api..."
                        )
                     }

                     when (val resultGetTasksAtApi = taskUseCase.getTasksAtApi()) {
                        is Result.Error -> _popups.value += Popup(
                           "ERROR",
                           "Failed to get tasks at api...",
                           resultGetTasksAtApi.error.toString()
                        )

                        is Result.Success -> {
                           val tasksApi = resultGetTasksAtApi.data
                           var lastResult2 = true

                           for (task in tasksApi) {
                              lastResult2 = when (val resultUpdateAtDatabase =
                                 taskUseCase.upsertTaskAtDatabase(task)) {
                                 is Result.Error -> false
                                 is Result.Success -> true
                              }
                           }

                           if (!lastResult2) {
                              _popups.value += Popup("ERROR", "Failed to save tasks locally...")
                           }
                        }
                     }

                     when (val result = userUseCase.getUserForProfileAtApi()) {
                        is Result.Error -> {
                           _popups.value += Popup(
                              "ERROR",
                              "Failed to get user profile...",
                              result.error.toString()
                           )
                        }

                        is Result.Success -> {
                           when (val re = userUseCase.upsertUserForProfileAtDatabase(result.data)) {
                              is Result.Error -> _popups.value += Popup(
                                 "ERROR",
                                 "Failed to save user locally...",
                                 re.error.toString()
                              )

                              is Result.Success -> {
                                 _popups.value += Popup("INFO", "Successfully logged in!") {
                                    onSuccess()
                                 }
                              }
                           }
                        }
                     }
                  }
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