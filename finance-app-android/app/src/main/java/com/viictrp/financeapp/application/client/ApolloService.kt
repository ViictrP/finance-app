package com.viictrp.financeapp.application.client

import com.apollographql.apollo3.ApolloClient

object ApolloClientProvider {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("http://192.168.1.5:8080/financeapp/graphql")
        .build()
}