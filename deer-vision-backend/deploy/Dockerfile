FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY deer-vision.jar .
EXPOSE 5000
CMD ["java", "-jar", "-Dserver.port=8080", "deer-vision.jar"]
