#/bin/bash

# Start postgres in docker container
docker run -d -p 5432:5432 --name my-postgres -e POSTGRES_PASSWORD=mysecretpassword postgres

# Create db and insert the commands from the sql file
PGPASSWORD=mysecretpassword psql -h localhost -p 5432 -U postgres -c 'create database vocabularies;'
PGPASSWORD=mysecretpassword psql -h localhost -p 5432 -U postgres -c '\i vocabularies.sql' -d vocabularies
