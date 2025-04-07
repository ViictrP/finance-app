package com.viictrp.financeapp.application.client

import com.apollographql.apollo3.ApolloClient
import com.viictrp.financeapp.application.service.interceptor.AuthInterceptor

object ApolloClientProvider {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://backend.financeapp.dev/financeapp/graphql")
        .addHttpInterceptor(AuthInterceptor())
        .build()
}