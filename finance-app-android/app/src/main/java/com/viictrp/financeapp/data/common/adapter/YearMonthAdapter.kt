package com.viictrp.financeapp.data.common.adapter

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object YearMonthAdapter : Adapter<YearMonth> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): YearMonth {
        val value = reader.nextString()
        return YearMonth.parse(value, formatter)
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, value: YearMonth) {
        writer.value(value.format(formatter))
    }
}