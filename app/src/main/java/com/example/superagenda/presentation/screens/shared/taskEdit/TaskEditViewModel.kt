package com.example.superagenda.presentation.screens.shared.taskEdit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.screens.shared.GlobalVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase,
    private val globalVariables: GlobalVariables
) : ViewModel() {
    val taskToEdit = globalVariables.getTaskToEdit()

    private val _title = MutableLiveData<String>()
    val title = _title

    private val _description = MutableLiveData<String>()
    val description = _description

    private val _taskStatus = MutableLiveData<TaskStatus>()
    val taskStatus = _taskStatus

    fun onShow() {
        _title.postValue(taskToEdit.value?.title ?: return)
        _description.postValue(taskToEdit.value?.description ?: return)
        _taskStatus.postValue(taskToEdit.value?.status ?: return)
    }

    fun onUpdateButtonPress() {
        viewModelScope.launch {
            Log.d("LOOK AT ME", "updatepress: ${taskToEdit.value}")


            val taskToUpdate = taskToEdit.value?.let {
                title.value?.let { it1 ->
                    description.value?.let { it2 ->
                        taskStatus.value?.let { it3 ->
                            Task(
                                _id = it._id,
                                title = it1,
                                description = it2,
                                status = it3
                            )
                        }
                    }
                }
            }


            if (taskToUpdate != null) {
                taskUseCase.updateTask(taskToUpdate)
            }
        }
    }

    fun onTitleChange(title: String) {
        _title.postValue(title)
    }

    fun onDescriptionChange(description: String) {
        _description.postValue(description)
    }

}