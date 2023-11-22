FROM openjdk:17-jdk-slim

WORKDIR /app
COPY deer-vision.jar .
EXPOSE 8080
#CMD ["java", "-jar", "-Dserver.port=8080", "deer-vision.jar"]
CMD sh -c 'java -jar -Dserver.port=8080 deer-vision.jar || tail -f /dev/null'