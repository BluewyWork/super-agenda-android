package com.example.superagenda.data.network

import com.example.superagenda.data.models.UserForLoginModel
import com.example.superagenda.data.network.response.ApiResponse
import com.example.superagenda.data.network.response.TokenBody
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi
{
   @POST(Endpoints.LOGIN_USER)
   suspend fun login(@Body user: UserForLoginModel): ApiResponse<TokenBody>
}