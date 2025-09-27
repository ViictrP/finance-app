package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.domain.repository.BalanceRepository
import javax.inject.Inject

class LoadInstallmentsUseCase @Inject constructor(
    private val repository: BalanceRepository
) {
    suspend operator fun invoke(installmentId: String): List<TransactionDTO?> {
        return repository.loadInstallments(installmentId)
    }
}
