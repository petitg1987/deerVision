# UrchinReleaseMgt
## Description
Release management application for Urchin engine

- Issues:
  - API to receive issues
  - Web interface to see issues
  
- Binaries:
  - API to upload binaries on AWS S3
  - API to download binaries and retrieve last binaries version
  - Web interface to see statistics of binaries


## Setup
### Jar
- Build Jar: `mvn clean package`
- Launch Jar: `java -jar ./target/urchin-release-mgt-[VERSION].jar`

### Docker
- Create Docker image: `mvn install dockerfile:build`
- Run Docker: `docker run urchin/urchin-release-mgt`

### AWS Elastic Beanstalk
- Create Zip package: `mvn clean package`
- Zip location: target/
