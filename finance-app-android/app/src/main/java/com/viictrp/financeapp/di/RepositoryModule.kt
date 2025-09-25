package com.viictrp.financeapp.di

import com.viictrp.financeapp.data.repository.AuthRepositoryImpl
import com.viictrp.financeapp.data.repository.BalanceRepositoryImpl
import com.viictrp.financeapp.domain.repository.AuthRepository
import com.viictrp.financeapp.domain.repository.BalanceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBalanceRepository(
        balanceRepositoryImpl: BalanceRepositoryImpl
    ): BalanceRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
