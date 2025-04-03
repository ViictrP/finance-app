package com.viictrp.financeapp.application.client

import com.apollographql.apollo3.ApolloClient

object ApolloClientProvider {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://backend.financeapp.dev/financeapp/graphql")
        .build()
}