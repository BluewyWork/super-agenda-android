package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.models.UserForRegisterModel
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.network.RegisterApi
import com.example.superagenda.domain.models.UserForRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val registerApi: RegisterApi
){
    suspend fun register(userForRegister: UserForRegister): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val userForRegisterModel = userForRegister.toData()

                val response = registerApi.register(userForRegisterModel)

                response.ok
            } catch(e: Exception) {
               Log.e("LOOK AT ME", "${e.message}")

                false
            }
        }
    }
}