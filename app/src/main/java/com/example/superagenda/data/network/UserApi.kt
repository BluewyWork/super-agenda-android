package com.example.superagenda.data.network

import com.example.superagenda.data.models.UserForProfileModel
import com.example.superagenda.data.network.models.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {
   @GET(Endpoints.GET_PROFILE)
   suspend fun getUserForProfile(@Header("Authorization") token: String): ApiResponse<UserForProfileModel>

   @DELETE(Endpoints.DELETE_PROFILE)
   suspend fun delete(@Header("Authorization") token: String): ApiResponse<Map<String, Any>>
}