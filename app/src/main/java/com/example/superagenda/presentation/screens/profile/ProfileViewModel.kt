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
import com.example.superagenda.domain.SelfUseCase
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
   private val selfUseCase: SelfUseCase,
   private val loginUseCase: LoginUseCase,
   private val taskUseCase: TaskUseCase
) : ViewModel() {
   private val _userForProfile = MutableLiveData<UserForProfile?>()
   val userForProfile: LiveData<UserForProfile?> = _userForProfile

   private val _popupsQueue = MutableLiveData<List<Pair<String, String>>>()
   val popupsQueue: LiveData<List<Pair<String, String>>> = _popupsQueue

   fun enqueuePopup(title: String, message: String) {
      _popupsQueue.value =
         popupsQueue.value?.plus(Pair(title, message)) ?: listOf(
            Pair(
               title,
               message
            )
         )
   }

   fun dismissPopup() {
      _popupsQueue.postValue(_popupsQueue.value?.drop(1))
   }

   fun waitForPopup(code: () -> Unit) {
      popupsQueue.observeForever { queue ->
         if (queue.isNullOrEmpty()) {
            code()
            popupsQueue.removeObserver { this }
         }
      }
   }

   fun onShow() {
      viewModelScope.launch {
         val userProfile = selfUseCase.retrieveUserForProfile()

         _userForProfile.postValue(userProfile)
      }
   }

   fun onDeleteButtonPressButton(navController: NavController) {
      viewModelScope.launch {
         if (!selfUseCase.deleteProfile()) {
            enqueuePopup("ERROR", "Failed to delete profile at api...")
            return@launch
         }

         if (!loginUseCase.clearTokensAtLocalStorage()) {
            enqueuePopup("ERROR", "Failed to clear token from local storage...")
            return@launch
         }

         if (!taskUseCase.deleteAllTasksAtLocalDatabase()) {
            enqueuePopup("ERROR", "Failed to clear tasks from local storage...")
            return@launch
         }

         enqueuePopup(
            "INFO",
            "Successfully deleted profile at api and cleared related data locally!"
         )

         waitForPopup {
            navController.navigate(Destinations.Login.route)
         }
      }
   }

   fun onLogoutPress(navController: NavController) {
      viewModelScope.launch {
         if (!loginUseCase.clearTokensAtLocalStorage()) {
            enqueuePopup("ERROR", "Failed to clear token from local storage...")
            return@launch
         }

         if (!taskUseCase.deleteAllTasksAtLocalDatabase()) {
            enqueuePopup("ERROR", "Failed to clear tasks from local storage...")
            return@launch
         }

         enqueuePopup("INFO", "Successfully logged out and cleared related data...")

         waitForPopup {
            navController.navigate(Destinations.Login.route)
         }
      }
   }

   fun onBackupButtonPress() {
      viewModelScope.launch {

      }
   }

   fun onImportButtonPress(contentResolver: ContentResolver, filePath: String) {
      viewModelScope.launch {

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
      filePath: String,
   ): List<Task> {
      val gson = Gson()
      val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
      val reader = BufferedReader(InputStreamReader(inputStream))

      val tasks: List<TaskModel> = gson.fromJson(reader, Array<TaskModel>::class.java).toList()
      reader.close()

      return tasks.map { it.toDomain() }
   }
}
