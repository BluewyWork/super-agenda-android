package com.example.superagenda.presentation.screens.shared.tasksOnGoing

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
class TasksOngoingViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase
) : ViewModel() {
    private val _onGoingTaskList = MutableLiveData<List<Task>?>()
    val onGoingTaskList: LiveData<List<Task>?> = _onGoingTaskList

    fun onShow() {
        viewModelScope.launch {
            val onGoingTaskList = taskUseCase.retrieveOnGoingTaskList()
            _onGoingTaskList.postValue(onGoingTaskList)
        }
    }
}