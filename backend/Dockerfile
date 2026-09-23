# Сборка Spring Boot backend в отдельном Maven-слое.
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw.cmd ./

RUN mvn -B dependency:go-offline -DskipTests

COPY src src

RUN mvn -B clean package -DskipTests

# Минимальный runtime-образ только с JRE и готовым JAR.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ENV SERVER_PORT=8080

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
