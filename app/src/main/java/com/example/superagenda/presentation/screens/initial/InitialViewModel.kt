package com.example.superagenda.presentation.screens.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.LastModifiedUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitialViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val lastModifiedUseCase: LastModifiedUseCase,
) : ViewModel() {

   private val _showLoading = MutableStateFlow(true)
   val showLoading: StateFlow<Boolean> = _showLoading

   private val _popupsQueue = MutableStateFlow<List<Triple<String, String, String>>>(emptyList())
   val popupsQueue: StateFlow<List<Triple<String, String, String>>> = _popupsQueue

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

   fun refreshTasksIfOutdated(onDone: () -> Unit) {
      viewModelScope.launch {
         withShowLoading {
            val resultGetLastModifiedLocally = lastModifiedUseCase.getLastModifiedAtDatabase()
            val resultGetLastModifiedRemote = lastModifiedUseCase.getLastModifiedAtApi()

            if (resultGetLastModifiedLocally !is Result.Success) {
               return@withShowLoading
            }

            if (resultGetLastModifiedRemote !is Result.Success) {
               return@withShowLoading
            }

            val lastModifiedLocally = resultGetLastModifiedLocally.data
            val lastModifiedRemote = resultGetLastModifiedRemote.data

            if (lastModifiedLocally == lastModifiedRemote) {
               return@withShowLoading
            }

            when (val resultGetTasksRemote = taskUseCase.getTasksAtApi()) {
               is Result.Error -> {
                  return@withShowLoading
               }

               is Result.Success -> {
                  var lastResult = false;

                  for (task in resultGetTasksRemote.data) {
                     lastResult = when (taskUseCase.upsertTaskAtDatabase(task)) {
                        is Result.Error -> false
                        is Result.Success -> true
                     }
                  }

                  if (!lastResult) {
                     TODO()
                  }
               }
            }

            onDone()
         }
      }
   }
}