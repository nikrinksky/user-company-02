FROM openjdk:17

WORKDIR /app
COPY target/service-discovery-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]