FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copia os arquivos do maven wrapper e dependências
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copia o código-fonte e compila
COPY src/ src/
RUN ./mvnw clean package -DskipTests

# Estágio final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
