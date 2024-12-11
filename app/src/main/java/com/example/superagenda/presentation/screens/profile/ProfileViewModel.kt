package com.example.superagenda.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.UserUseCase
import com.example.superagenda.domain.models.Membership
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.util.Result
import com.example.superagenda.util.onError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val userUseCase: UserUseCase,
   private val authenticationUseCase: AuthenticationUseCase,
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
   private val _userForProfile = MutableStateFlow(
      UserForProfile(
         username = "", membership = Membership.FREE
      )
   )

   val userForProfile: StateFlow<UserForProfile> = _userForProfile.onStart {
      viewModelScope.launch {
         when (val result = userUseCase.getUserForProfileAtDatabase()) {
            is Result.Error -> _popups.value += Popup(
               "ERROR", "Failed to get profile locally...)", result.error.toString()
            )

            is Result.Success -> {
               _userForProfile.value = result.data
            }
         }
      }
   }.stateIn(
      viewModelScope, SharingStarted.WhileSubscribed(5000L), UserForProfile(
         username = "", membership = Membership.FREE
      )
   )

   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popupsQueue: StateFlow<List<Popup>> = _popups

   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   fun onRefreshProfilePress() {
      fetchUserForProfile()
   }

   fun onDeleteButtonPressButton(navController: NavController) {
      viewModelScope.launch {
         when (val resultDeleteUserAtApi = userUseCase.deleteUserAtApi()) {
            is Result.Error -> _popups.value += Popup(
               "ERROR", "Failed to delete profile at api...", resultDeleteUserAtApi.error.toString()
            )

            is Result.Success -> {
               if (!resultDeleteUserAtApi.data) {
                  return@launch
               }

               when (val deleteTokenAtDatabase = authenticationUseCase.deleteTokenFromDatabase()) {
                  is Result.Error -> _popups.value += Popup(
                     "ERROR",
                     "Failed to clear token from local storage...",
                     deleteTokenAtDatabase.error.toString()
                  )

                  is Result.Success -> {
                     when (val resultDeleteTasksAtDatabase = taskUseCase.deleteTasksAtDatabase()) {
                        is Result.Error -> {
                           _popups.value += Popup(
                              "ERROR",
                              "Failed to clear tasks from local storage...",
                              resultDeleteTasksAtDatabase.error.toString()
                           )
                        }

                        is Result.Success -> {
                           _popups.value += Popup(
                              "INFO",
                              "Successfully deleted profile at api and cleared related data locally!"
                           ) {
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

   fun onLogoutPressed(navController: NavController) {
      viewModelScope.launch {
         authenticationUseCase.deleteTokenFromDatabase().onError { error ->
            _popups.value += Popup(
               "ERROR", "Failed to clear token from local storage...", error.toString()
            )
            return@launch
         }

         when (val resultDeleteTasksAtDatabase = taskUseCase.deleteTasksAtDatabase()) {
            is Result.Error -> {
               _popups.value += Popup(
                  "ERROR",
                  "Failed to clear tasks from local storage...",
                  resultDeleteTasksAtDatabase.error.toString()
               )
            }

            is Result.Success -> {
               _popups.value += Popup(
                  "INFO", "Successfully logged out and cleared related data..."
               ) {
                  navController.navigate(Destinations.Login.route)
               }
            }
         }
      }
   }

   private fun fetchUserForProfile() {
      viewModelScope.launch {
         when (val result = userUseCase.getUserForProfileAtApi()) {
            is Result.Error -> {
               _popups.value += Popup(
                  "ERROR", "Failed to get user profile...", result.error.toString()
               )
            }

            is Result.Success -> {
               when (val re = userUseCase.upsertUserForProfileAtDatabase(result.data)) {
                  is Result.Error -> _popups.value += Popup(
                     "ERROR", "Failed to save user locally...", re.error.toString()
                  )

                  is Result.Success -> {
                     _popups.value += Popup("INFO", "Successfully refreshed profile!")
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
