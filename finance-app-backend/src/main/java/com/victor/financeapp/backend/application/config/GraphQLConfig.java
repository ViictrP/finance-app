package com.victor.financeapp.backend.application.config;

import com.victor.financeapp.backend.application.config.scalar.YearMonthScalar;
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
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType yearMonthScalar) {
        return wiringBuilder -> wiringBuilder.scalar(yearMonthScalar);
    }
}
