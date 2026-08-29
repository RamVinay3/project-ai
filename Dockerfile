# Stage 1: Build the application (ensure 'AS build' is present)
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime environment
FROM eclipse-temurin:21-jre

WORKDIR /app

# Install unzip to unpack the Base64 decoded wallet
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

# Copy the JAR from the 'build' stage above
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE ${PORT}

# Decode the wallet from Render secret file and launch Spring Boot
ENTRYPOINT ["/bin/sh", "-c", "\
    if [ -f /etc/secrets/wallet.b64 ]; then \
      mkdir -p /app/wallet && \
      base64 -d /etc/secrets/wallet.b64 > /app/wallet/wallet.zip && \
      unzip -o /app/wallet/wallet.zip -d /app/wallet; \
    fi && \
    java -XX:+UseSerialGC -Xss512k -XX:MaxRAMPercentage=75.0 \
         -Doracle.net.tns_admin=/app/wallet \
         -Dserver.port=${PORT} \
         -jar app.jar"]