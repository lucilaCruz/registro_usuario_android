package com.example.proyectoregistro.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/users/create")
    suspend fun registro(@Body requestBody: RequestBody):Response<ResponseBody>
}