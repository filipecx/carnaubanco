#Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /app    

#Cache inteligente de dependências do maven
COPY pom.xml .
RUN mvn dependency:go-offline -B

#Copia o código fonte e empacota o jar
COPY src ./src
RUN mvn clean package -DskipTests -B

#Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

#Criação de usuário não root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

#copia o artefato buildado na etapa anterior
COPY --from=builder --chown=spring:spring /app/target/*jar app.jar

#Configurações jvm para baixo consumo de memoria e fuso utc
ENV JAVA_OPTS="-Xms64m -Xmx128m -XX:+UseG1GC -Duser.timezone=UTC"
ENV SERVER_PORT=8081

ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -jar app.jar" ]