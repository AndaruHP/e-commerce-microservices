# syntax=docker/dockerfile:1.4
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY security-common/pom.xml security-common/pom.xml
COPY security-common/src security-common/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f security-common/pom.xml clean install

COPY order-service/pom.xml order-service/pom.xml
COPY order-service/src order-service/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f order-service/pom.xml package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/order-service/target/order-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app/app.jar"]