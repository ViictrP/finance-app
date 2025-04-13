package com.viictrp.financeapp.application.service.mapper

import com.viictrp.financeapp.application.dto.MonthClosureDTO
import com.viictrp.financeapp.graphql.GetBalanceQuery

internal fun mapMonthClosureDTO(closures: List<GetBalanceQuery.MonthClosure?>): List<MonthClosureDTO> =
    closures
        .filterNotNull()
        .map { closure ->
            MonthClosureDTO(
                month = closure.month,
                year = closure.year,
                total = closure.total,
                available = closure.available,
                expenses = closure.expenses,
                index = closure.index,
                finalUsdToBRL = closure.finalUsdToBRL
            )
        }