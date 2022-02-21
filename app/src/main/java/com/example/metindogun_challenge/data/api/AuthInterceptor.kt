package com.example.metindogun_challenge.data.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val httpUrl = originalRequest.url().newBuilder()
            .addQueryParameter("api_key", apiKey)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(httpUrl)

        return chain.proceed(requestBuilder.build())

    }
}