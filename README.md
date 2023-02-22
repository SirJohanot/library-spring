# library-spring

<p align="center">
    <img alt="License" src="https://img.shields.io/github/license/SirJohanot/library-spring">
    <img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SirJohanot/library-spring?display_name=version">
</p>

## About

A web application that simulates a library, complete with an account system and multiple user roles (reader, librarian,
administrator). This project runs as a REST API.

### Technology stack used for this project:

- Spring Boot;
- Spring Security;
- Hibernate ORM;
- PostgreSQL.

## Installation

1. Download the `library-spring-x.x.x.jar`
2. Create an `application.properties` file in the same directory as the `.jar` file
3. In `application.properties`, specify the database url, database username, database password, driver class name and
   hibernate dialect per Spring Boot `application.properties` specifications. Otherwise, the application will fall back
   to the default parameters
4. Run `library-spring-x.x.x.jar` (requires Java Runtime Environment 17 or newer)

## Developers

- [Johanot](https://github.com/SirJohanot)
- [kolosw](https://github.com/kolosw)

## License

Project Library Spring is distributed under the MIT license.