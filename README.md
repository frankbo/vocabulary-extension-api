# vocabulary-extension-api
Backend for the vocabulary browser extension

## Run locally
To run the api locally use `sbt run`

## Run local postgres with some data
`chmod +x database/createDB.sh && ./database/createDB.sh`

## ONLY Run local postgres db with docker
 `docker run --name postgri -e POSTGRES_PASSWORD=mysecretpassword -d postgres`