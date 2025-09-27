package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.domain.repository.BalanceRepository
import javax.inject.Inject

class DeleteTransactionWithCacheUseCase @Inject constructor(
    private val repository: BalanceRepository
) {
    suspend operator fun invoke(id: Long, all: Boolean) {
        repository.deleteTransaction(id, all)
        repository.clearCache()
    }
}
