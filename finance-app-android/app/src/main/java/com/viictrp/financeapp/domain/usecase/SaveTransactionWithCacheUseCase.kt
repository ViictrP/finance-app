package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.TransactionDTO
import com.viictrp.financeapp.domain.repository.BalanceRepository
import javax.inject.Inject

class SaveTransactionWithCacheUseCase @Inject constructor(
    private val repository: BalanceRepository
) {
    suspend operator fun invoke(transaction: TransactionDTO): TransactionDTO? {
        val result = if (transaction.creditCardId != null) {
            repository.saveCreditCardTransaction(transaction)
        } else {
            repository.saveTransaction(transaction)
        }
        repository.clearCache()
        return result
    }
}
