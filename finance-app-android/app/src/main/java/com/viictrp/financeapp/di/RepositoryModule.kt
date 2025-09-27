package com.viictrp.financeapp.di

import com.viictrp.financeapp.data.repository.AuthRepositoryImpl
import com.viictrp.financeapp.data.repository.BalanceRepositoryImpl
import com.viictrp.financeapp.data.repository.UserRepositoryImpl
import com.viictrp.financeapp.domain.repository.AuthRepository
import com.viictrp.financeapp.domain.repository.BalanceRepository
import com.viictrp.financeapp.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
