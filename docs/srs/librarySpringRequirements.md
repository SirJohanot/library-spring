# Requirements Document

## 1 Introduction

This is a requirements document for the back-end part of the ‘Library Spring’ project. This project’s
aim and goal is to simulate a real library, complete with an account
system, different user roles, checking out and returning books and editing
the library’s contents.

## 2 User Requirements

### 2.1 Software Interfaces

The Library will interact with a variety of external sources and systems:

* Apache Tomcat – Library Spring is, in itself, a REST API,
  which will use Apache Tomcat for deployment;
* PostgreSQL – the database of choice will be PostgreSQL, where data on books,
  users and other business entities will be stored.

### 2.2 User Interfaces

The user will interact with the system using some front-end application to make requests to the REST API.

### 2.3 User Characteristics

Ease of use will be among the Library’s top-priority design goals. The
application itself is designed to be implemented both in education and in
leisure.

### 2.4 Assumptions and Dependencies

Factors that affect the requirements:

* Database connectivity and stability;
* Effective and optimized business logic.

## 3 System Requirements

### 3.1 Functional Requirements

* users have to be able to create accounts, log into accounts and change
  their account’s details (name, surname, username);
* each user account has to have a role corresponding to their duties and
  access level (reader, librarian, administrator);
* administrators have to be able to edit the Library’s contents (delete
  books, edit books, add books);
* administrators have to be able to edit user statuses (block accounts, edit
  user account details, assign roles to users);
* librarians have to be able to approve/deny reader’s book checkout
  requests, resolve readers returning books;
* readers have to be able to rent books, return books;
* all data should be persisted in a PostgreSQL database.

### 3.2 Non-Functional Requirements

#### 3.2.1 SOFTWARE QUALITY ATTRIBUTES

* ease of use – important for even the least tech-savvy user to get the hang
  of using the application’s features;
* security – important to secure each user’s account details.
