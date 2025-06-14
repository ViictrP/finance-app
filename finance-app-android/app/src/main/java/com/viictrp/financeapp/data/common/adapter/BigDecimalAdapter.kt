package com.viictrp.financeapp.data.common.adapter

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import java.math.BigDecimal

object BigDecimalAdapter : Adapter<BigDecimal> {

    override fun fromJson(
        reader: JsonReader,
        customScalarAdapters: CustomScalarAdapters
    ): BigDecimal {
        return reader.nextString()?.toBigDecimal() ?: BigDecimal.ZERO
    }

    override fun toJson(
        writer: JsonWriter,
        customScalarAdapters: CustomScalarAdapters,
        value: BigDecimal
    ) {
        writer.value(value.toString())
    }
}