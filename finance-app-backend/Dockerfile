# Etapa de build com Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache de dependências
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN ./mvnw dependency:go-offline

# Copia código-fonte e compila
COPY src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV ISSUER_URI=$ISSUER_URI
ENV JWK_SET_URI=$JWK_SET_URI
ENV SCHEMA=$SCHEMA

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./app.jar"]