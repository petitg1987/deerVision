FROM openjdk:11
EXPOSE 5000
COPY /target/urchin-release-mgt-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]