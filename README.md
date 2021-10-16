<h2 align="center">Deer Vision Website</h2>
https://deervision.studio

# Features
- Games presentation:
  - Photon Engineer
- Administration console:
  - Interface to manage issues
  - Interface to see usage statistics

# Deploy
## Local
- Backend:
  - Move in backend folder: `cd deer-vision-backend`
  - Build Jar: `mvn clean package`
  - Launch Jar: `java -jar ./target/deer-vision-[VERSION].jar`
- Frontend:
  - Move in backend folder: `cd deer-vision-frontend`
  - Execute: `yarn start`

## AWS
- See [here](setup/README.md)
