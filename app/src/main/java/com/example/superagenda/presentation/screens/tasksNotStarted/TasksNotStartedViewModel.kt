package com.example.superagenda.presentation.screens.tasksNotStarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksNotStartedViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase
) : ViewModel() {
    private val _notStartedTaskList = MutableLiveData<List<Task>?>()
    val notStartedTaskList: LiveData<List<Task>?> = _notStartedTaskList

    fun onShow() {
        viewModelScope.launch {
            val notStartedTaskList = taskUseCase.retrieveNotStartedTaskList()
            _notStartedTaskList.postValue(notStartedTaskList)
        }
    }
}