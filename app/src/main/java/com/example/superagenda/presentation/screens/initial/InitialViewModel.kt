package com.example.superagenda.presentation.screens.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.MiscUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.TheRestUseCase
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val theRestUseCase: TheRestUseCase,
   private val authenticationUseCase: AuthenticationUseCase,
   private val miscUseCase: MiscUseCase,
) : ViewModel() {

   private val _showLoading = MutableStateFlow(true)
   val showLoading: StateFlow<Boolean> = _showLoading

   private val _popupsQueue = MutableStateFlow<List<Triple<String, String, String>>>(emptyList())
   val popupsQueue: StateFlow<List<Triple<String, String, String>>> = _popupsQueue

   private val _navDecision = MutableStateFlow(TheDecision.UNDECIDED)

   val navDecision: StateFlow<TheDecision> = _navDecision.onStart {
      viewModelScope.launch {
         val sliderShown = when (miscUseCase.getScreenShownAtDatabase()) {
            is Result.Error -> false
            is Result.Success -> true
         }

         if (!sliderShown) {
            _navDecision.value = TheDecision.NAV_SLIDER
            return@launch
         }

         refreshTasksIfOutdated()
         _navDecision.value = TheDecision.NAV_TASKS
      }
   }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TheDecision.UNDECIDED)

   // Utilities

   private fun enqueuePopup(title: String, message: String, error: String = "") {
      _popupsQueue.value =
         popupsQueue.value + Triple(title, message, error)
   }

   fun dismissPopup() {
      _popupsQueue.value = _popupsQueue.value.drop(1)
   }

   private suspend fun whenPopupsEmpty(code: () -> Unit) {
      // if this is null it will execute the code, hmm....
      while (popupsQueue.value.isNotEmpty()) {
         delay(2000)
      }

      code()
   }

   private suspend fun <T> withShowLoading(code: suspend () -> T): T {
      return try {
         _showLoading.value = true
         code()
      } catch (e: Exception) {
         throw e
      } finally {
         _showLoading.value = false
      }
   }

   // Main

   private suspend fun refreshTasksIfOutdated() {
      withShowLoading {
         val resultLoggedIn = authenticationUseCase.isLoggedIn()

         if (resultLoggedIn !is Result.Success) {
            return@withShowLoading
         }

         val resultGetLastModifiedLocally = theRestUseCase.getLastModifiedAtDatabase()
         val resultGetLastModifiedRemote = theRestUseCase.getLastModifiedAtApi()

         if (resultGetLastModifiedLocally !is Result.Success) {
            return@withShowLoading
         }

         if (resultGetLastModifiedRemote !is Result.Success) {
            return@withShowLoading
         }

         val lastModifiedLocally = resultGetLastModifiedLocally.data
         val lastModifiedRemote = resultGetLastModifiedRemote.data

         // this is unnecessary check since
         // on failure equals null or non existent
         // which logic is same as here
         if (lastModifiedLocally == null) {
            return@withShowLoading
         }

         if (lastModifiedRemote == null) {
            return@withShowLoading
         }

         if (lastModifiedLocally == lastModifiedRemote) {
            return@withShowLoading
         }

         val resultGetTasksRemote = taskUseCase.getTasksAtApi()
         val resultGetTasksDatabase = taskUseCase.getTasksAtDatabase()

         if (resultGetTasksRemote !is Result.Success) {
            return@withShowLoading
         }

         if (resultGetTasksDatabase !is Result.Success) {
            return@withShowLoading
         }

         if (lastModifiedLocally < lastModifiedRemote) {
            val tasksDeleted =
               resultGetTasksDatabase.data.filter { it !in resultGetTasksRemote.data }

            var lastResult2 = false;

            for (task in tasksDeleted) {
               lastResult2 = when (taskUseCase.deleteTaskAtDatabase(task.id)) {
                  is Result.Error -> false
                  is Result.Success -> true
               }
            }

            var lastResult = false;

            for (task in resultGetTasksRemote.data) {
               lastResult = when (taskUseCase.upsertTaskAtDatabase(task)) {
                  is Result.Error -> false
                  is Result.Success -> true
               }
            }
         } else if (lastModifiedLocally > lastModifiedRemote) {
            val tasksDeleted =
               resultGetTasksRemote.data.filter { it !in resultGetTasksDatabase.data }

            var lastResult = false

            for (task in tasksDeleted) {
               lastResult = when (taskUseCase.deleteTaskAtApi(task.id)) {
                  is Result.Error -> false
                  is Result.Success -> true
               }
            }

            var lastResult2 = false

            for (task in resultGetTasksDatabase.data) {
               lastResult2 = when (taskUseCase.updateTaskAtApi(task)) {
                  is Result.Error -> false
                  is Result.Success -> true
               }
            }
         }
      }
   }
}

enum class TheDecision {
   NAV_SLIDER,
   NAV_TASKS,
   UNDECIDED
}
