package com.example.superagenda.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.UserUseCase
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
   private val loginUseCase: LoginUseCase,
   private val taskUseCase: TaskUseCase,
   private val userUseCase: UserUseCase,
) : ViewModel() {
   private val _username = MutableLiveData<String>()
   val username: LiveData<String> = _username

   private val _password = MutableLiveData<String>()
   val password: LiveData<String> = _password

   private val _popupsQueue = MutableLiveData<List<Triple<String, String, String>>>()
   val popupsQueue: LiveData<List<Triple<String, String, String>>> = _popupsQueue

   fun onUsernameChange(username: String) {
      _username.postValue(username)
   }

   fun onPasswordChange(password: String) {
      _password.postValue(password)
   }

   private fun enqueuePopup(title: String, message: String, error: String = "") {
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
      // if this is null it will execute the code, hmm....
      while (popupsQueue.value?.isNotEmpty() == true) {
         delay(2000)
      }

      code()
   }

   fun onLoginButtonPress(navController: NavController) {
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

         when (val resultLogin = loginUseCase.login(
            UserForLogin(
               username,
               password
            )
         )) {
            is Result.Error -> enqueuePopup("ERROR", "Failed to log in...", resultLogin.error.toString())

            is Result.Success -> {
               _password.postValue("")
               enqueuePopup("INFO", "Successfully logged in!")

               when (val resultGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
                  is Result.Error ->
                     enqueuePopup("ERROR", "Failed to get tasks at api...")

                  is Result.Success -> {
                     val tasksDatabase = resultGetTasksAtDatabase.data
                     var lastResult = true

                     for (task in tasksDatabase) {
                        lastResult = when (val resultCreateTaskAtApi = taskUseCase.createTaskAtApi(task)) {
                           is Result.Error -> {
                     //                              enqueuePopup(
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
                        enqueuePopup(
                           "ERROR",
                           "Failed to create tasks at api..."
                        )
                     }

                     when (val resultGetTasksAtApi = taskUseCase.getTasksAtApi()) {
                        is Result.Error -> enqueuePopup(
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
                              enqueuePopup("ERROR", "Failed to save tasks locally...")
                           }
                        }
                     }

                     when (val result = userUseCase.getUserForProfileAtApi()) {
                        is Result.Error -> {
                           enqueuePopup("ERROR", "Failed to get user profile...", result.error.toString())
                        }

                        is Result.Success -> {
                           userUseCase.upsertUserForProfileAtDatabase(result.data)
                        }
                     }

                     whenPopupsEmpty {
                        navController.navigate(Destinations.Tasks.route)
                     }
                  }
               }
            }
         }
      }
   }
}