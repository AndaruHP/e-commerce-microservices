# syntax=docker/dockerfile:1.4
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY security-common/pom.xml security-common/pom.xml
COPY security-common/src security-common/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f security-common/pom.xml clean install

COPY product-service/pom.xml product-service/pom.xml
COPY product-service/src product-service/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f product-service/pom.xml clean package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/product-service/target/product-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]