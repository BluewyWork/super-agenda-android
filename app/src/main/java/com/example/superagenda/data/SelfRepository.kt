package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.SelfApi
import com.example.superagenda.domain.models.UserForProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SelfRepository @Inject constructor(
   private val selfApi: SelfApi,
) {
   suspend fun retrieveProfileFromAPI(token: String): UserForProfile? {
      return withContext(Dispatchers.IO) {
         try {
            selfApi.retrieveUserProfile(token).data.toDomain()
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            null
         }
      }
   }

   suspend fun deleteProfileFromApi(token: String): Boolean {
      return withContext(Dispatchers.IO) {
         try {
            val response = selfApi.deleteProfile(token)

            return@withContext response.ok
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "${e.message}")

            false
         }
      }
   }
}