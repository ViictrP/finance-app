package com.victor.financeapp.backend.adapter.config.scalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class YearMonthScalar {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    public static final GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("YearMonth")
            .description("A custom scalar to handle Java YearMonth")
            .coercing(new Coercing<YearMonth, String>() {
                @Override
                public String serialize(Object dataFetcherResult) {
                    if (dataFetcherResult instanceof YearMonth) {
                        return ((YearMonth) dataFetcherResult).format(FORMATTER);
                    }
                    throw new IllegalArgumentException("Invalid YearMonth format");
                }

                @Override
                public YearMonth parseValue(Object input) {
                    if (input instanceof String) {
                        return YearMonth.parse((String) input, FORMATTER);
                    }
                    throw new IllegalArgumentException("Invalid YearMonth format");
                }

                @Override
                public YearMonth parseLiteral(Object input) {
                    if (input instanceof StringValue) {
                        return YearMonth.parse(((StringValue) input).getValue(), FORMATTER);
                    }
                    throw new IllegalArgumentException("Invalid YearMonth format");
                }
            })
            .build();
}
