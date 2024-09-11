package com.example.superagenda.domain

import com.example.superagenda.data.DeletedTaskRepository
import com.example.superagenda.data.LoginRepository
import org.bson.types.ObjectId
import javax.inject.Inject

class DeletedTaskUseCase @Inject constructor(
   private val deletedTaskRepository: DeletedTaskRepository,
   private val loginRepository: LoginRepository
){
   suspend fun retrieveDeletedTasksAtApi(): List<ObjectId>? {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return null
      }

      return deletedTaskRepository.retrieveDeletedTasksAtApi(token)
   }

   suspend fun addDeletedTaskAtApi(deletedTaskId: ObjectId): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      return deletedTaskRepository.addDeletedTaskAtApi(token, deletedTaskId)
   }
}