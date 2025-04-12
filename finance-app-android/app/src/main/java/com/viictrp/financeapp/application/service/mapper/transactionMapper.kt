package com.viictrp.financeapp.application.service.mapper

import com.viictrp.financeapp.application.dto.TransactionDTO

internal fun <T> mapTransactionDTO(
    items: List<T?>,
    toDTO: (T) -> TransactionDTO
): List<TransactionDTO> {
    return items.filterNotNull().map(toDTO)
}
