package com.example.superagenda.data.network

import com.example.superagenda.data.network.models.ApiResponse
import com.example.superagenda.data.network.models.LastModifiedInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MiscApi {
   @GET(Endpoints.GET_LAST_MODIFIED)
   suspend fun getLastModified(
      @Header("Authorization") token: String,
   ): Response<ApiResponse<LastModifiedInResponse>>

   @PATCH(Endpoints.UPDATE_LAST_MODIFIED)
   suspend fun updateLastModified(
      @Header("Authorization") token: String,
      @Body lastModified: LastModifiedInResponse,
   ): Response<ApiResponse<Nothing>>
}