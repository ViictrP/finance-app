package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.InvoiceDTO
import com.viictrp.financeapp.domain.repository.BalanceRepository
import java.time.YearMonth
import javax.inject.Inject

class GetInvoiceUseCase @Inject constructor(
    private val repository: BalanceRepository
) {
    suspend operator fun invoke(creditCardId: Long, yearMonth: YearMonth): InvoiceDTO? {
        return repository.getInvoice(creditCardId, yearMonth)
    }
}
