# ── Stage 1: Build the jar using Maven wrapper ──
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x ./mvnw

RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw clean package -DskipTests

# ── Stage 2: Lightweight runtime image ──
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/mindshift-backend-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]