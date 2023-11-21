FROM openjdk:17-jre-slim

WORKDIR /usr/src/app
COPY deer-vision.jar .
EXPOSE 8080
# Specify any environment variables (if needed)
# ENV VARIABLE_NAME=value
CMD ["java", "-jar", "-Dserver.port=8080", "deer-vision.jar"]