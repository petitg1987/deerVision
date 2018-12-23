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

### AWS Elastic Beanstalk
- Create Zip package: `mvn clean package`
- Zip location: target/
