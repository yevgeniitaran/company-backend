## Company-backend

Company-backend is a small Spring Boot RESTful project that consists of authentication, authorization and CRUD operations for Users and Companies.

## Start project

Requirements: JDK 1.8. To start project, execute in project folder

    mvnw spring-boot:run

## Example of usage

Authorization and receiving JWT token:

    curl -d "username=admin&password=admin" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:8080/v1/public/users/login

GET request example using received JWT token:

    curl -H "Authorization:eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSSkpMzk7NS1HSUcpMLFGyMjQ1NjE1NzU1N9BRSq0ogAiYmRlaggRKi1OL8hJzU4HaElNyM_OUagHvp-zjRgAAAA.61Wdx4cVGXv3ZEfHYKf0Z1-BmqsyJyQWzlTfxEq2glo" -X GET http://localhost:8080/v1/users

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/358f9aaa21f9a812afef)
