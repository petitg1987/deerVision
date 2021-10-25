<h2 align="center">Deer Vision Website</h2>
https://deervision.studio

# Content
- Studio presentation
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
  - Move in frontend folder: `cd deer-vision-frontend`
  - Execute (one shot): `yarn install`
  - Execute: `yarn start`

## AWS
- See [here](setup/README.md)
