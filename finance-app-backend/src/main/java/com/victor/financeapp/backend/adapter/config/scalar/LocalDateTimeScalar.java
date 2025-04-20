package com.victor.financeapp.backend.adapter.config.scalar;

import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeScalar {

    public static final GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("LocalDateTime")
            .description("java.time.LocalDateTime")
            .coercing(new Coercing<LocalDateTime, String>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                @Override
                public String serialize(Object dataFetcherResult) {
                    return formatter.format((LocalDateTime) dataFetcherResult);
                }

                @Override
                public LocalDateTime parseValue(Object input) {
                    String raw = input.toString().replace("Z", "");
                    return LocalDateTime.parse(raw, formatter);
                }

                @Override
                public LocalDateTime parseLiteral(Object input) {
                    String raw = input.toString().replace("Z", "");
                    return LocalDateTime.parse(raw, formatter);
                }
            }).build();
}
