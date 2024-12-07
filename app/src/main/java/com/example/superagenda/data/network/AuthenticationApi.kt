package com.example.superagenda.data.network

import com.example.superagenda.data.models.UserForLoginModel
import com.example.superagenda.data.models.UserForRegisterModel
import com.example.superagenda.data.network.models.ApiResponse
import com.example.superagenda.data.network.models.TokenInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
   @POST(Endpoints.LOGIN_USER)
   suspend fun login(@Body user: UserForLoginModel): Response<ApiResponse<TokenInResponse>>

   @POST(Endpoints.REGISTER_USER)
   suspend fun register(@Body user: UserForRegisterModel): Response<ApiResponse<Nothing>>
}