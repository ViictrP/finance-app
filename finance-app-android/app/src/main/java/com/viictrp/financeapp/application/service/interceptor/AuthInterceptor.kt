package com.viictrp.financeapp.application.service.interceptor

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await


class AuthInterceptor() : HttpInterceptor {

    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        var token = FirebaseAuth.getInstance()
            .currentUser?.getIdToken(false)
            ?.await()
            ?.token

        val authRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(authRequest)

        if (response.statusCode == 401) {
            return mutex.withLock {
                val refreshedToken = FirebaseAuth.getInstance()
                    .currentUser?.getIdToken(true)
                    ?.await()
                    ?.token

                val retryRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer $refreshedToken")
                    .build()

                chain.proceed(retryRequest)
            }
        }

        return response
    }

}