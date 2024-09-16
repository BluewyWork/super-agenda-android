package com.example.superagenda.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.UserForLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
   private val loginUseCase: LoginUseCase,
   private val taskUseCase: TaskUseCase,
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

   fun waitForPopup(code: () -> Unit) {
      popupsQueue.observeForever { queue ->
         if (queue.isNullOrEmpty()) {
            code()
            popupsQueue.removeObserver { this }
         }
      }
   }

   fun onShow(navController: NavController) {
      viewModelScope.launch {
         val isLoggedIn = loginUseCase.isLoggedIn()

         if (!isLoggedIn) {
            return@launch
         }

         // NOTE: with the current flow of the application this code is unused.
         navController.navigate(Destinations.Tasks.route)
         _password.postValue("")
      }
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

         val ok = loginUseCase.login(
            UserForLogin(
               username,
               password
            )
         )

         if (ok) {
            // attempt to save local tasks
            val tasks = taskUseCase.retrieveTasksFromLocalDatabase()

            var lastResult = true

            if (tasks != null) {
               for (task in tasks) {
                  lastResult = taskUseCase.createTaskAtAPI(task)
               }
            }

            if (!lastResult) {
               enqueuePopup("ERROR", "Failed to sync tasks created before logged in...")
            }

            val remoteTasks = taskUseCase.retrieveTaskAtApi()

            var lastResult2 = true

            if (remoteTasks != null) {
               for (task in remoteTasks) {
                  lastResult2 = taskUseCase.insertOrUpdateTaskAtLocalDatabase(task)
               }
            }

            if (!lastResult2) {
               enqueuePopup("ERROR", "Failed to bring some or all tasks locally...")
            }

            _password.postValue("")
            enqueuePopup("INFO", "Successfully logged in!")

            while (popupsQueue.value?.isNotEmpty() == true) {
               delay(1000)
            }

            navController.navigateUp()
         } else {
            enqueuePopup("ERROR", "Something went wrong...")
         }
      }
   }
}