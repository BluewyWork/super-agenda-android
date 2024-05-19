package com.example.superagenda.data.network

import com.example.superagenda.data.models.UserForProfileModel
import com.example.superagenda.data.network.response.ApiResponse
import com.example.superagenda.domain.models.UserForProfile
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {
    @GET(Endpoints.GET_PROFILE)
    suspend fun retrieveUserProfile(@Header("Authorization") token: String): ApiResponse<UserForProfileModel>
}