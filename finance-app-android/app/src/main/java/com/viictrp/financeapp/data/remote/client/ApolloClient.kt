package com.viictrp.financeapp.data.remote.client

import com.apollographql.apollo3.ApolloClient
import com.viictrp.financeapp.data.remote.interceptor.AuthInterceptor

object ApolloClientProvider {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://backend.financeapp.dev/financeapp/graphql")
        .addHttpInterceptor(AuthInterceptor())
        .build()
}