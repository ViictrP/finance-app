package com.viictrp.financeapp.data.remote.client

import com.apollographql.apollo3.ApolloClient
import com.viictrp.financeapp.data.remote.interceptor.AuthInterceptor

object ApolloClientProvider {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("http://192.168.1.5:8080/financeapp/graphql")
        .addHttpInterceptor(AuthInterceptor())
        .build()
}