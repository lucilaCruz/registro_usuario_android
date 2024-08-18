package com.example.proyectoregistro.repository

import com.example.proyectoregistro.api.AuthService
import com.example.proyectoregistro.beans.Usuario
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit

class AuthRespository {
    suspend fun registrarse(usuario:Usuario): Response<ResponseBody>{
        var retrofit = Retrofit.Builder().baseUrl("https://miprimersistemaweb.com").build()
        var service = retrofit.create(AuthService::class.java)
        //implementar el json
        val bodyJson = JSONObject()
        bodyJson.put("name",usuario.name)
        bodyJson.put("email",usuario.email)
        bodyJson.put("password",usuario.password)
        val bodyJsonString = bodyJson.toString()
        val requestBody:RequestBody = bodyJsonString.toRequestBody("application/json".toMediaTypeOrNull())
        return  service.registro(requestBody)
    }

}