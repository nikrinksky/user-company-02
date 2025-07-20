FROM openjdk:17

WORKDIR /app
COPY target/api-gateway-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]