# UrchinReleaseMgt
## Description
Release management application for Urchin engine

- Issues management:
  - API to receive issues
  - Web interface to see issues
  
- Binaries management:
  - API to upload binaries on AWS S3
  - API to download binaries and retrieve last binaries version
  - Web interface to see statistics of binaries

## Install
### Local
- Build Jar: `mvn clean package`
- Launch Jar: `java -jar ./target/urchin-release-mgt-[VERSION].jar`

### AWS
- See [here](./setup/README.md)