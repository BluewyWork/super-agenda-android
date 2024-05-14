package com.example.superagenda.presentation.screens.shared.taskEdit

import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.screens.shared.tasksNotStarted.TasksNotStartedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase
) : ViewModel(){
    val _taskToEdit = MutableLiveData<Task>()
    var taskToEdit: LiveData<Task> = _taskToEdit

    fun setTaskToEdit(objectId: ObjectId) {

    }
}