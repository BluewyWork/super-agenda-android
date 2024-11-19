package com.example.superagenda.presentation.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.UserUseCase
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.util.Result
import com.example.superagenda.util.onError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val userUseCase: UserUseCase,
   private val loginUseCase: LoginUseCase,
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
   private val _userForProfile = MutableLiveData<UserForProfile?>()
   val userForProfile: LiveData<UserForProfile?> = _userForProfile

   private val _popupsQueue = MutableLiveData<List<Pair<String, String>>>()
   val popupsQueue: LiveData<List<Pair<String, String>>> = _popupsQueue

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

   fun onShow() {
      viewModelScope.launch {
         when (val result = userUseCase.getUserForProfileAtApi()) {
            is Result.Error -> enqueuePopup("ERROR", result.error.toString())
            is Result.Success -> _userForProfile.postValue(result.data)
         }
      }
   }

   fun onDeleteButtonPressButton(navController: NavController) {
      viewModelScope.launch {
         when (val resultDeleteUserAtApi = userUseCase.deleteUserAtApi()) {
            is Result.Error -> enqueuePopup("ERROR", "Failed to delete profile at api...")

            is Result.Success -> {
               if (!resultDeleteUserAtApi.data) {
                  return@launch
               }

               when (val deleteTokenAtDatabase = loginUseCase.deleteTokenFromDatabase()) {
                  is Result.Error -> enqueuePopup(
                     "ERROR",
                     "Failed to clear token from local storage..."
                  )

                  is Result.Success -> {
                     when (val resultDeleteTasksAtDatabase = taskUseCase.deleteTasksAtDatabase()) {
                        is Result.Error -> {
                           enqueuePopup("ERROR", "Failed to clear tasks from local storage...")
                           return@launch
                        }

                        is Result.Success -> {
                           enqueuePopup(
                              "INFO",
                              "Successfully deleted profile at api and cleared related data locally!"
                           )

                           waitForPopup {
                              navController.navigate(Destinations.Login.route)
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   fun onLogoutPress(navController: NavController) {
      viewModelScope.launch {
         loginUseCase.deleteTokenFromDatabase().onError {
            enqueuePopup("ERROR", "Failed to clear token from local storage...")
            return@launch
         }

         when (val resultDeleteTasksAtDatabse = taskUseCase.deleteTasksAtDatabase()) {
            is Result.Error -> {
               enqueuePopup("ERROR", "Failed to clear tasks from local storage...")
            }

            is Result.Success -> {
               enqueuePopup("INFO", "Successfully logged out and cleared related data...")

               waitForPopup {
                  navController.navigate(Destinations.Login.route)
               }
            }
         }
      }
   }
}
