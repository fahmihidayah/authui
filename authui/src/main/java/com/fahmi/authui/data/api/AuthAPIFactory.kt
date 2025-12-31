package com.fahmi.authui.data.api

object AuthAPIFactory {

    fun createOkHttpClient() : okhttp3.OkHttpClient {
        val builder = okhttp3.OkHttpClient.Builder()

        if (com.fahmi.authui.AuthUi.enableLog) {
            val logging = okhttp3.logging.HttpLoggingInterceptor()
            logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(logging)
        }

        return builder.build()
    }

    fun createRetrofit(): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder()
            .baseUrl(com.fahmi.authui.AuthUi.baseURL)
            .client(createOkHttpClient())
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
    }

    fun createAuthAPI(): AuthAPI {
        return createRetrofit().create(AuthAPI::class.java)
    }
}