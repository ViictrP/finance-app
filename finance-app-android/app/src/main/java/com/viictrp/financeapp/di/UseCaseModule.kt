package com.viictrp.financeapp.di

import com.viictrp.financeapp.domain.repository.AuthRepository
import com.viictrp.financeapp.domain.repository.BalanceRepository
import com.viictrp.financeapp.domain.usecase.CheckAuthUseCase
import com.viictrp.financeapp.domain.usecase.DeleteTransactionUseCase
import com.viictrp.financeapp.domain.usecase.GetCurrentBalanceUseCase
import com.viictrp.financeapp.domain.usecase.GetInvoiceUseCase
import com.viictrp.financeapp.domain.usecase.LoadInstallmentsUseCase
import com.viictrp.financeapp.domain.usecase.LoginWithGoogleUseCase
import com.viictrp.financeapp.domain.usecase.LogoutUseCase
import com.viictrp.financeapp.domain.usecase.SaveCreditCardUseCase
import com.viictrp.financeapp.domain.usecase.SaveTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCurrentBalanceUseCase(
        repository: BalanceRepository
    ): GetCurrentBalanceUseCase = GetCurrentBalanceUseCase(repository)

    @Provides
    fun provideGetInvoiceUseCase(
        repository: BalanceRepository
    ): GetInvoiceUseCase = GetInvoiceUseCase(repository)

    @Provides
    fun provideSaveTransactionUseCase(
        repository: BalanceRepository
    ): SaveTransactionUseCase = SaveTransactionUseCase(repository)

    @Provides
    fun provideSaveCreditCardUseCase(
        repository: BalanceRepository
    ): SaveCreditCardUseCase = SaveCreditCardUseCase(repository)

    @Provides
    fun provideDeleteTransactionUseCase(
        repository: BalanceRepository
    ): DeleteTransactionUseCase = DeleteTransactionUseCase(repository)

    @Provides
    fun provideLoadInstallmentsUseCase(
        repository: BalanceRepository
    ): LoadInstallmentsUseCase = LoadInstallmentsUseCase(repository)

    @Provides
    fun provideCheckAuthUseCase(
        repository: AuthRepository
    ): CheckAuthUseCase = CheckAuthUseCase(repository)

    @Provides
    fun provideLoginWithGoogleUseCase(
        repository: AuthRepository
    ): LoginWithGoogleUseCase = LoginWithGoogleUseCase(repository)

    @Provides
    fun provideLogoutUseCase(
        repository: AuthRepository
    ): LogoutUseCase = LogoutUseCase(repository)
}
