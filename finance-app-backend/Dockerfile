FROM maven:3.9.9-eclipse-temurin-21 AS mvn
WORKDIR /usr/src/app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:21-slim AS java
WORKDIR /usr/src/app
COPY --from=mvn /usr/src/app/target/*.jar ./app.jar

ENV PORT=$PORT
ENV DATABASE_URL=$DATABASE_URL
ENV DB_USER=$DB_USER
ENV DB_PASSWORD=$DB_PASSWORD
ENV ISSUER_URI=$ISSUER_URI
ENV JWK_SET_URI=$JWK_SET_URI
ENV SCHEMA=$SCHEMA

ENTRYPOINT ["java", "-jar", "./app.jar"]
EXPOSE $PORT