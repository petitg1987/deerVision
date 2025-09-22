FROM openjdk:21-jdk-slim

WORKDIR /app
COPY deer-vision.jar .

EXPOSE 8080
ENV DB_PASSWORD=toDefine
ENV ADMIN_PASSWORD=toDefine
ENV ADMIN_JWT_SECRET=toDefine

CMD sh -c 'java -jar -Dserver.port=8080 deer-vision.jar || tail -f /dev/null'
