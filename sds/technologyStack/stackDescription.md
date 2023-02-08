# Development

## Programming language

Java 17 will be the language of choice for this project. Its libraries and APIs offer a wide range of functionalities
for working with web applications.

## Build tool

Gradle will be used to build, test and deploy the Library Spring application.

## Database

To store information on business entities, Library Spring will use a PostgreSQL database.

## Dependencies

* Spring Boot - the framework which offers great functionality for creating web-based REST APIs;
* Spring Data JPA - a module which is needed to use Java Object Relational Mapping for streamlining the interactions
  with databases;
* PostgreSQL Connector - needed for the ORM to properly work with a PostgreSQL database;
* Spring Security - a module which will be used to implement role-based authorization;
* Spring Validation - a module which will be used to validate the data the application will receive.

# Deployment

The end product will be deployed using Apache Tomcat.