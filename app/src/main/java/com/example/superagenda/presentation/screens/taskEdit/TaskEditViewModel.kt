package com.example.superagenda.presentation.screens.taskEdit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.screens.GlobalVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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

    private val _startDateTime = MutableLiveData<LocalDateTime>()
    val startDateTime: LiveData<LocalDateTime> = _startDateTime

    private val _endDateTime = MutableLiveData<LocalDateTime>()
    val endDateTime: LiveData<LocalDateTime> = _endDateTime

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun onError(message: String) {
        _errorMessage.postValue(message)
    }

    fun onErrorDismissed() {
        _errorMessage.postValue(null)
    }

    fun onShow() {
        _title.postValue(taskToEdit.value?.title ?: return)
        _description.postValue(taskToEdit.value?.description ?: return)
        _taskStatus.postValue(taskToEdit.value?.status ?: return)
        Log.d("LOOK AT ME", "CHEK STATUS: ${_taskStatus.value}")
        _startDateTime.postValue(taskToEdit.value?.startDateTime ?: return)
        _endDateTime.postValue(taskToEdit.value?.endDateTime ?: return)
    }

    fun onUpdateButtonPress(navController: NavController) {
        viewModelScope.launch {

            val taskToUpdate = taskToEdit.value?.let {
                title.value?.let { it1 ->
                    description.value?.let { it2 ->
                        taskStatus.value?.let { it3 ->
                            _startDateTime.value?.let { it4 ->
                                _endDateTime.value?.let { it5 ->
                                    Task(
                                        _id = it._id,
                                        title = it1,
                                        description = it2,
                                        status = it3,
                                        startDateTime = it4,
                                        endDateTime = it5
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (taskToUpdate == null) {
                return@launch
            }

            if (!taskUseCase.definitiveCreateOrUpdateTask(taskToUpdate)) {
                onError("Failed for local database to insert task... HOW?")

                return@launch
            }

            if (!taskUseCase.definitiveSynchronizeUpTaskList()) {
                onError("Failed for API to update task..\n\nMaybe check your internet connection?\n\nSynchronization will happen on the next successful action.")

                return@launch
            }

            navController.popBackStack()
        }
    }

    fun onDeleteButtonPress(navController: NavController) {
        viewModelScope.launch {
            val taskToEdit = taskToEdit.value ?: return@launch

            if (!taskUseCase.deleteTask(taskToEdit)) {
                onError("Failed for API to delete task...\nMaybe check your internet connection?")

                return@launch
            }

            if (!taskUseCase.definitiveDeleteTask(taskToEdit._id.toHexString())) {
                onError("Failed for local database to delete task... HOW?")

                return@launch
            }

            navController.popBackStack()
        }
    }

    fun onTitleChange(title: String) {
        _title.postValue(title)
    }

    fun onDescriptionChange(description: String) {
        _description.postValue(description)
    }

    fun onTaskStatusChange(taskStatus: TaskStatus) {
        Log.d("LOOK AT ME", "ON task status change: $taskStatus")
        _taskStatus.postValue(taskStatus)
    }

    fun onStartDateTimeChange(startDatetime: LocalDateTime) {
        _startDateTime.postValue(startDatetime)
    }

    fun onEndDateTimeChange(endDateTime: LocalDateTime) {
        _endDateTime.postValue(endDateTime)
    }
}