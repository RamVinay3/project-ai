FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE ${PORT}

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