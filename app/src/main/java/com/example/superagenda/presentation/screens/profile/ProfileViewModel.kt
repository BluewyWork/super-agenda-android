package com.example.superagenda.presentation.screens.profile

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.ProfileUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.UserForProfile
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val taskUseCase: TaskUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _userForProfile = MutableLiveData<UserForProfile?>()
    val userForProfile: LiveData<UserForProfile?> = _userForProfile

    fun onShow() {
        viewModelScope.launch {
            val userProfile = profileUseCase.retrieveUserForProfile()

            _userForProfile.postValue(userProfile)
        }
    }

    fun onDeleteButtonPressButton(navController: NavController) {
        viewModelScope.launch {
            if (!profileUseCase.deleteProfile()) {
                return@launch
            }

            navController.navigate(Destinations.Login.route)
        }
    }

    fun onBackupButtonPress() {
        viewModelScope.launch {
            taskUseCase.saveTaskListToLocalStorage()
        }
    }

    fun onImportButtonPress(contentResolver: ContentResolver, filePath: String) {
        viewModelScope.launch {
            Log.d("LOOK AT ME", "CHOSEN FILE: ${printFileContents(contentResolver, filePath)}")
            val taskList = deserializeTasksFromJson(contentResolver, filePath)

            for (task in taskList) {
                Log.d("LOOK AT ME", "ITS WORKING: $task")
            }

            taskUseCase.importTaskListFromLocalStorage(taskList)
        }
    }

    fun onLogoutPress(navController: NavController) {
        viewModelScope.launch {
            if (!loginUseCase.logout()) {
                return@launch
            }

            navController.navigate(Destinations.Login.route)
        }
    }

    private fun printFileContents(contentResolver: ContentResolver, filePath: String) {
        val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            stringBuilder.append(line).append('\n')
            line = reader.readLine()
        }
        inputStream?.close()
        reader.close()
        val fileContent = stringBuilder.toString()
        Log.d("LOOK AT ME", "File Content:\n$fileContent")
    }


    private fun deserializeTasksFromJson(
        contentResolver: ContentResolver,
        filePath: String
    ): List<Task> {
        val gson = Gson()
        val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
        val reader = BufferedReader(InputStreamReader(inputStream))

        val tasks: List<TaskModel> = gson.fromJson(reader, Array<TaskModel>::class.java).toList()
        reader.close()

        return tasks.map { it.toDomain() }
    }
}
