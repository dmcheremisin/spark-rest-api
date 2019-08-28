[![CircleCI](https://circleci.com/gh/dmcheremisin/spark-rest-api.svg?style=svg)](https://circleci.com/gh/dmcheremisin/spark-rest-api)

Project - Simple rest api for money transfers without Spring

### AWS for test
AWS instance for test: http://ec2-18-224-25-189.us-east-2.compute.amazonaws.com:8080

### Collection for postman of rest calls
Postman Collection: notForBuild/Rest Api.postman_collection.json

### How to run project?
1. Build project using maven from the root pom file
2. Go to web/target folder
3. Execute command: java -jar web-1.0-SNAPSHOT-jar-with-dependencies.jar

### Task
The task was to build rest api using any light-weight frameworks or no frameworks. 
The solution should run as a standalone application, so it is assumed that any in-memory db should be used.
I heard some time ago about Spark(Web, not big data) from course of MongoDb University as rest light weight framework that is very simple.
So, I googled some information about it and decided to use it for my solution.
What about some db persistence framework? I could use hibernate or myBatis, some people call them light-weight too, but is depends=)
I decided to use a new tool for java-db object mapping called sql2o. It has very easy syntax and it is truly light.

### Preliminary work 
I modeled entities in jHipster for the database. Db model may be found in the initial commit in notForBuild top folder.
Than I decided to model the project structure. 
Multi-module architecture has a lot of advantages, so I decided to split project to 2 packages: db and web.

I wrote all issues right here using github Issue Manager. 
History may be found here: https://github.com/dmcheremisin/spark-rest-api/issues


### Technology stack:
- Java 8
- Spark java web framework
- Embeded H2 database
- Sql20 - database query library
- HikariCP connection pool
- Project Lombok
- Gson
- jUnit 4
- Mockito

### Continuous integration
I had already experience of using Jenkins for continuos integration, but I prefere CircleCI.
It is free, easy and has very good integration with github.
Every commit I make is tested the same time by CircleCi automatically.
Another advantage is that I can see status of build right in the repo README.md file(top of this page).

### Docker
File for creation Docker image may be found in the folder notForBuild/Dockerfile.
Instructions are here: notForBuild/docker commands.md

### AWS
I also tested my solution on AWS free tier.
I used Ubuntu 18.04 as image. Link to the application may be found on the top of the page.

