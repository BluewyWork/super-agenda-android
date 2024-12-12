package com.example.superagenda.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val taskUseCase: TaskUseCase) : ViewModel() {
   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popups: StateFlow<List<Popup>> = _popups

   private val _taskToEdit = MutableStateFlow<Task?>(null)
   val taskToEdit: StateFlow<Task?> = _taskToEdit

   private val _tasks = MutableStateFlow<List<Task>>(emptyList())

   val tasks: StateFlow<List<Task>> =
      _tasks.onStart {
         viewModelScope.launch {
            when (val resultTaskGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
               is Result.Error -> {
                  _popups.value += Popup(
                     "ERROR",
                     "Failed to get tasks locally...",
                     resultTaskGetTasksAtDatabase.error.toString()
                  )
               }

               is Result.Success -> {
                  _tasks.value = resultTaskGetTasksAtDatabase.data
               }
            }
         }
      }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   fun onEditPressed(task: Task) {
      _taskToEdit.value = task
   }

   fun getTaskToEdit(): Task? {
      return _taskToEdit.value
   }
}

data class Popup(
   val title: String = "",
   val description: String = "",
   val error: String = "",
   val code: () -> Unit = {},
)
