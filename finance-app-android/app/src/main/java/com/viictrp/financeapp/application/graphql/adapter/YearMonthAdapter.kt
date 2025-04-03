package com.viictrp.financeapp.application.graphql.adapter

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object YearMonthAdapter : Adapter<YearMonth> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM")

    override fun fromJson(reader: JsonReader, customScalarAdapters: com.apollographql.apollo3.api.CustomScalarAdapters): YearMonth {
        val value = reader.nextString()
        return YearMonth.parse(value, formatter)
    }

    override fun toJson(writer: JsonWriter, customScalarAdapters: com.apollographql.apollo3.api.CustomScalarAdapters, value: YearMonth) {
        writer.value(value.format(formatter))
    }
}