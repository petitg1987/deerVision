# Deer Vision Website
## URL
https://deervision.studio

## Website Content
* Showcasing the games developed by Deer Vision studio
* API for the games:
  * API to send games statistics
  * API to send crash reports
* Administration:
  * Interface to visualize the games statistics
  * Interface to visualize the crash reports

## Website Deployment
* Local
  * Backend:
    * Move in backend folder: `cd deer-vision-backend`
    * Build the Jar: `mvn clean package`
    * Launch the Jar: `java -jar ./target/deer-vision-[VERSION].jar`
  * Frontend:
    * Move in frontend folder: `cd deer-vision-frontend`
    * Execute (one shot): `yarn install`
    * Execute: `yarn start`
* AWS
  * See [here](setup/README.md)
