package com.example.superagenda.data.network

import android.util.Log
import com.example.superagenda.data.models.UserForLoginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationService @Inject constructor(
    private val authenticationApi: AuthenticationApi
){
    suspend fun login(userForLoginModel: UserForLoginModel): String? {
        return withContext(Dispatchers.IO) {
            try {
                val x = authenticationApi.login(userForLoginModel)

                x.data.token
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }
}