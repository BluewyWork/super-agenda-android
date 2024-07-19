package com.example.superagenda.presentation.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.superagenda.domain.models.Task
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalVariables @Inject constructor(

) {
   private val _taskToEdit = MutableLiveData<Task>()

   fun getTaskToEdit(): LiveData<Task> {
      Log.d("LOOK AT ME", "--> ${_taskToEdit.value?.status}")
      return _taskToEdit
   }

   fun setTaskToEdit(task: Task) {
      _taskToEdit.postValue(task)
   }
}