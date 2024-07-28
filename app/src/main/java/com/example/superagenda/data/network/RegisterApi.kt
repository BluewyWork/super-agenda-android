package com.example.superagenda.data.network

import com.example.superagenda.data.models.UserForRegisterModel
import com.example.superagenda.data.network.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi
{
   @POST(Endpoints.REGISTER_USER)
   suspend fun register(@Body user: UserForRegisterModel): ApiResponse<Map<String, Any>>
}