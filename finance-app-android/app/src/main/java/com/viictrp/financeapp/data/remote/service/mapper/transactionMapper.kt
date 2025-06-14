package com.viictrp.financeapp.data.remote.service.mapper

import com.viictrp.financeapp.data.remote.dto.TransactionDTO

internal fun <T> mapTransactionDTO(
    items: List<T?>,
    toDTO: (T) -> TransactionDTO
): List<TransactionDTO> {
    return items.filterNotNull().map(toDTO)
}
