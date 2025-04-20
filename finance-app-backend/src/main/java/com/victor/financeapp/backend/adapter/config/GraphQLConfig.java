package com.victor.financeapp.backend.adapter.config;

import com.victor.financeapp.backend.adapter.config.scalar.LocalDateTimeScalar;
import com.victor.financeapp.backend.adapter.config.scalar.YearMonthScalar;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQLScalarType yearMonthScalar() {
        return YearMonthScalar.INSTANCE;
    }

    @Bean
    public GraphQLScalarType localDateTimeScalar() {
        return LocalDateTimeScalar.INSTANCE;
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType yearMonthScalar, GraphQLScalarType localDateTimeScalar) {
        return wiringBuilder -> wiringBuilder
                .scalar(yearMonthScalar)
                .scalar(localDateTimeScalar)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.GraphQLBigDecimal)
                .scalar(ExtendedScalars.Json)
                .scalar(ExtendedScalars.LocalTime);
    }
}
