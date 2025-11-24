FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/plataforma-de-feedback-0.0.1-SNAPSHOTS.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
