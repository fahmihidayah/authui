package com.fahmi.authui.data.api

import com.fahmi.authui.data.request.LoginForm
import com.fahmi.authui.data.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI  {
    @POST("login")
    suspend fun login(@Body loginForm: LoginForm): Response<LoginResponse>
}
