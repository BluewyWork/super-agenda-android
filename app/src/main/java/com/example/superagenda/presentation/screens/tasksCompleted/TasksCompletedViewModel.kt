package com.example.superagenda.presentation.screens.tasksCompleted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.screens.GlobalVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksCompletedViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase,
    private val globalVariables: GlobalVariables
) : ViewModel() {
    private val _completedTaskList = MutableLiveData<List<Task>?>()
    val completedTaskList: LiveData<List<Task>?> = _completedTaskList

    fun onShow() {
        viewModelScope.launch {
            val completedTaskList = taskUseCase.retrieveCompletedTaskList2()
            _completedTaskList.postValue(completedTaskList)
        }
    }

    fun onEditClick(task: Task) {
        globalVariables.setTaskToEdit(task)
    }
}