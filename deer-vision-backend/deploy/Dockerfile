FROM openjdk:17-jdk-slim

WORKDIR /app
COPY deer-vision.jar .

EXPOSE 8080
ENV DB_PASSWORD=toDefine
ENV ADMIN_PASSWORD=toDefine
ENV ADMIN_JWT_TOKEN=toDefine

CMD ["java", "-jar", "-Dserver.port=8080", "deer-vision.jar"]
