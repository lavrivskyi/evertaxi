# EverTaxi
<p align="center">
<img src="https://user-images.githubusercontent.com/85931447/139277578-ecc770e8-e296-4305-ba24-9cff46c4f0a0.png" alt="EverTaxi">
</p>

## Description
This project is a simple realization of "taxi service" system. User can sign up as a driver. Driver can see and manage information about available cars.<br/>
The project is N-tier application with: 
- DAO layer for interaction with DB.
- Service layer with business logic.
- Controller level for handling HTTP requests.
- Presentation level for user interaction with the application.<br/>

CRUD operations are used to manage and retrieve data from DB. "Soft delete" used for all delete operations.<br/>
Added 52 test classes with code lines coverage 88%

## Features
- Login. If wrong credentials provided, error message will be shown.
- Create new car.
- Show list of all cars with information about each car and list of drivers assigned to the car.
- Delete any car from the list. All drivers will be unassigned from deleted car.
- Create new driver.
- Add driver to car.
- Show list of all registered drivers with information about each driver. 
- Delete any driver from the list. Driver will be unassigned from previously assigned cars.
- Create new car manufacturer.
- Show list of all car's manufacturers with information about each manufacturer.
- Delete any manufacturer from the list.

## Technologies used
- Java 11
- Maven 3.1.1
- Maven Checkstyle Plugin
- JDBC
- JSP
- JSTL 1.2
- Javax Servlet API 4.0.1
- MySQL 8 RDBMS
- Apache Tomcat 9.0.54
- Log4j2
- JUnit 4.13.2
- Mockito 4.0.0

## Usage

1. For the correct work of the program you need to have MySQL and <a href="https://tomcat.apache.org/download-90.cgi">Apache Tomcat 9</a> installed
2. Fork and clone this project.
3. Initialize DB with `init_db.sql` file from `resources` directory. 
4. Add DB credentials to `ConnectionUtil` file from `util` directory.
      ```java
        private static final String URL = "jdbc:mysql://DATABASE_URL:PORT/taxi";
        private static final String USERNAME = USERNAME;
        private static final String PASSWORD = PASSWORD;
      ```
5. Add Tomcat configuration to your project.
   - Fix missing artifact. [EXAMPLE](https://cln.sh/4cj9kj)
   - Use `/` as your Tomcat application context. [EXAMPLE](https://cln.sh/d68iSq)
6. Configure `log4j2.xml` from `resources` directory. Add correct path to the `app.log` file. 
   - By default, you can find log file in your `Tomcat` folder: `.../Tomcat/apache-tomcat-9/bin/logs/app.log`
