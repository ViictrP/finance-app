package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.CreditCardDTO
import com.viictrp.financeapp.domain.repository.BalanceRepository
import javax.inject.Inject

class SaveCreditCardUseCase @Inject constructor(
    private val repository: BalanceRepository
) {
    suspend operator fun invoke(creditCard: CreditCardDTO): CreditCardDTO? {
        return repository.saveCreditCard(creditCard)
    }
}
