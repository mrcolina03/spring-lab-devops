# Etapa 1: Build
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar

# Etapa 2: Run
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Argumentos de build para versionamiento
ARG APP_VERSION=1.0.0-dev
ARG BUILD_NUMBER=local
ARG COMMIT_SHA=unknown

# Variables de entorno
ENV APP_VERSION=${APP_VERSION}
ENV BUILD_NUMBER=${BUILD_NUMBER}
ENV COMMIT_SHA=${COMMIT_SHA}

COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]