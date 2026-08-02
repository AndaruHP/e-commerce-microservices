# syntax=docker/dockerfile:1.4
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY auth-service/pom.xml auth-service/pom.xml
COPY auth-service/src auth-service/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f auth-service/pom.xml package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
