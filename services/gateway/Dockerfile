# syntax=docker/dockerfile:1.4
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY gateway/pom.xml gateway/pom.xml
COPY gateway/src gateway/src
RUN --mount=type=cache,target=/root/.m2 mvn -q -DskipTests -f gateway/pom.xml package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/gateway/target/gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
