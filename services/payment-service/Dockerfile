# syntax=docker/dockerfile:1.4
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY security-common/pom.xml security-common/pom.xml
COPY security-common/src security-common/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f security-common/pom.xml clean install

COPY payment-service/pom.xml payment-service/pom.xml
COPY payment-service/src payment-service/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f payment-service/pom.xml package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/payment-service/target/payment-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/app.jar"]