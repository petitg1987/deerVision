FROM openjdk:11
EXPOSE 5000
COPY /bin/urchin-release-mgt.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]