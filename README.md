![CI Status](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd/actions/workflows/tests.yml/badge.svg)

# Content
- [0. MoneyLion Assessment](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#0-moneylion-assessment)
    - [0.1 Demo and Artifacts](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#01-demo-and-artifacts)
    - [0.2 Server Configurations](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#02-server-configurations)
        - [0.2.1 Port](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#021-port)
        - [0.2.2 Database URL](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#022-database-url)
            - [0.2.2.1 MacOS or Linux](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#0221-macos-or-linux)
            - [0.2.2.2 WINDOWS (CMD)](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#0222-windows-cmd)
            - [0.2.2.3 Windows (Powershell)](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#0223-windows-powershell)
- [1. Problem Statement (User Story)](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#1-problem-statement-user-story)
    - [1.1 Minimum Requirements](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#11-minimum-requirements)
    - [1.2 Assumptions for Framework Selections](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#12-assumptions-for-framework-selections)
- [2. Architecture](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#2-architecture)
- [3. Database View](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#3-database-view)
- [4. Tests and Coverage](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#4-tests-and-coverage)
- [5. Demo](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#5-demo)
    - [5.1. API Based](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#51-api-based)
    - [5.2. UI Based](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#52-ui-based)
- [6. Future Plans](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#6-future-plans)


# 0. MoneyLion Assessment
This repository contains an implementation for a requested assessment. 
It will also act as a template for future projects and will continue to be maintained for development
purpose. Please feel free to use any section of this template for your future spring boot projects.

#### 0.1 Demo and Artifacts
- [API Demo](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#51-api-based)
- [UI Demo](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd#52-ui-based)
- [API Docs and Client Generators](https://editor.swagger.io/?url=https://raw.githubusercontent.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd/master/open-api-specification.yaml)
- [OpenAPI YAML Specifications](open-api-specification.yaml)
- [Download Pre-Release](https://github.com/ShamsUlAzeem/MoneyLionAssessmentEndToEnd/releases/download/v0.0.1/Assessment-0.0.1-SNAPSHOT.jar) *(Required Java 11)* Run by executing `java -jar Assessment-0.0.1-SNAPSHOT.jar` and navigate to http://localhost:8080. Later in the future, I will create native binaries without the need to install Java Development Kit or runtime

#### 0.2 Server Configurations
The server can be configured through the following options

##### 0.2.1 Port
For running on a different port you can use following `server.port` vm option

`java -jar Assessment-0.0.1-SNAPSHOT.jar -Dserver.port=9090`

##### 0.2.2 Database URL
For updating the database url you can either use `spring.datasource.url` vm options as:

`java -jar Assessment-0.0.1-SNAPSHOT.jar '-Dspring.datasource.url=jdbc:h2:file:./data/money-lion-assessment'`

OR

Update it through the `ASSESSMENT_DATABASE_URL` environment variable 

###### 0.2.2.1 MacOS or Linux
`export ASSESSMENT_DATABASE_URL=jdbc:h2:file:./data/money-lion-assessment`

###### 0.2.2.2 WINDOWS (CMD)
`SET ASSESSMENT_DATABASE_URL=jdbc:h2:file:./data/money-lion-assessment`

###### 0.2.2.3 Windows (Powershell)
`$Env:ASSESSMENT_DATABASE_URL = jdbc:h2:file:./data/money-lion-assessment`

# 1. Problem Statement (User Story)
As Product Manager, I would like to manage users’ accesses to new features via feature switches, 
i.e. enabling/disabling certain feature based on a user’s email and feature names).

## 1.1 Minimum Requirements
Main requirements for this assessment are implementation of the following two endpoints

1. `GET /feature?email=XXX&featureName=XXX`
    
    This endpoint receives (user’s email) and featureName as request parameters and returns the following response in JSON format.

    #### Example Response

   ```json
   {
       "canAccess": true|false (will be true if the user has access to the featureName)
   }
   ```
    
2. `POST /feature`
    
    This endpoint receives the following request in JSON format and returns an empty response with HTTP Status OK (200) 
    when the database is updated successfully, otherwise returns Http Status Not Modified (304).

    #### Example Request

    ```json
    {
      "featureName": "xxx", (string)
      "email": "xxx", (string) (user's name)
      "enable": true|false (boolean) (uses true to enable a user's access, otherwise)
    }
    ```

## 1.2 Assumptions for Framework Selections
Since the assessment indicates an open-ended solution, I've taken the following design considerations for the implementation.
1. Java as base language
2. Database (H2 selected here) capable of the following modes
   1. In-memory (for repeatable tests without teardown methods)
   2. Embedded file mode (for making executable package delivery simpler)
   3. Server mode (for concurrency, multi-access, scalability if needed)
3. UI platform for convenience (Vaadin chosen because of Java based UI builders)
4. Liquibase for keeping and validating migration checksum and versions
5. Security based features include
   1. JWT Token authentication
   2. CSRF tokens for UI based usage protection
   3. Last but not least a decent password encrpytion using Bcrypt
6. Spring Boot Application framework for main implementation
7. Testing framework using Mock Mvc framework
8. Planning for standalone native binaries for testing in systems without JDK or JVM runtime installed, using graalvm image builder
9. Planning for CI/CD using GitHub workflows 
10. Generation of OpenAPI specification from javax annotation framework for the ability to generate polyglot clients
11. Deployment on AWS EC2 instance for demo

# 2. Architecture
Since the endpoints use case needs a storage mechanism so, the data is stored in H2 database. Database changelog and 
migrations are validated through liquibase framework and is convenient for future proofing further updates.

Security is done through two mechanisms. One of them is through JWT token generation and validation through a custom 
created principal mechanism. This has been done to protect unwanted access to the mentioned endpoints above. The second 
protection mechanism is for protecting UI through CSRF token for preventing cross site request forgery and basic username
and password based authentication maintained through spring boot session IDs using browser cookies.

The following diagram shows, in a simplified manner, how each component interact with each other.

![img.png](architecture.png)

# 3. Database View

The top two tables show migration and versioning logs for liquibase framework and the 4 tables below is how features and
users are mapped together.

---
**NOTE**
![img.png](database-view.png)
The tables for AUTHORITIES and USERS are in total sync with the user principles of Spring boot security framework and are
also maintained through liquibase migration changelogs.

---
# 4. Tests and Coverage
The following image shows how much each Java class is covered through the implemented test cases

![img.png](test-coverage.png)

# 5. Demo
The following two demos shows how the application works in different modes

## 5.1. API Based
Using postman, the following workflow can be seen

![final_637a4273b61a7005ca19ac8b_309090](https://user-images.githubusercontent.com/17789400/202912506-644d4014-9f1c-4306-b37f-f01fabdf3aa5.gif)

## 5.2. UI Based
This is for added convenience for updating newly created users and features

![final_637a4273b61a7005ca19ac8b_595743](https://user-images.githubusercontent.com/17789400/202909923-76706f00-37b6-4763-a4d4-7b2e1a2406f2.gif)

# 6. Future Plans
Future plans includes:
1. Maintenance of CI/CD for the creation of native binaries for production releases using graalvm.
2. API documentation through javadoc comments and source code/api exploration through GitHub pages.
3. Better test coverage for UI.
