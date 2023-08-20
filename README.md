# library-spring

<p style="text-align: center;">
    <img alt="License" src="https://img.shields.io/github/license/SirJohanot/library-spring">
    <img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SirJohanot/library-spring?display_name=tag">
</p>

## About

A web application that simulates a library, complete with an account system and multiple user roles (reader, librarian,
administrator). This project runs as a REST API.

### Technology stack used for this project:

- Spring Boot
- Spring Security
- Hibernate ORM
- PostgreSQL

## Installation

1. Download the `library-spring-x.x.x.jar`
2. Create an `application.properties` file in the same directory as the `.jar` file
3. In `application.properties`, specify the url, username and password for the datasource per Spring
   Boot `application.properties` [specifications](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.datasource.configuration).
   Otherwise, the application will fall back
   to the default parameters
4. Run `library-spring-x.x.x.jar` using `java -jar library-spring-x.x.x.jar`

## Requirements

- [Java Runtime Environment](https://www.java.com/en/download/manual.jsp)
- [PostgreSQL](https://www.postgresql.org/download/) database

## Developers

- [Johanot](https://github.com/SirJohanot)
- [kolosw](https://github.com/kolosw)

## License

Project Library Spring is distributed under the MIT license.
