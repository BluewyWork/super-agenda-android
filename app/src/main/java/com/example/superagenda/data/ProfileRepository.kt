package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.ProfileApi
import com.example.superagenda.domain.models.UserForProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun retrieveUserProfile(token: String): UserForProfile? {
        return withContext(Dispatchers.IO) {
            try {
                profileApi.retrieveUserProfile(token).data.toDomain()
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }
}