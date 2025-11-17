# Etapa 1: Build
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootjar

# Etapa2: Run
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/ARCHIVO_INEXISTENTE.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]