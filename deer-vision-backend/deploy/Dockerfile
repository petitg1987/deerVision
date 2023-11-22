FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY deer-vision.jar .
EXPOSE 5000
#CMD ["java", "-jar", "-Dserver.port=5000", "deer-vision.jar"]
CMD sh -c 'java -jar -Dserver.port=5000 deer-vision.jar || tail -f /dev/null'