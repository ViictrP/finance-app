package com.viictrp.financeapp.domain.usecase

import com.viictrp.financeapp.data.remote.dto.BalanceDTO
import com.viictrp.financeapp.domain.repository.BalanceRepository
import java.time.YearMonth
import javax.inject.Inject

class GetCurrentBalanceUseCase @Inject constructor(
    private val repository: BalanceRepository
) {
    suspend operator fun invoke(yearMonth: YearMonth): BalanceDTO? {
        return repository.getBalance(yearMonth)
    }
}
