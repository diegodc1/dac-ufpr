# Dockerfile

# Etapa 1: build do projeto
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: imagem final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Executa o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
