# https://github.com/jerelquay/docker-maven-sample
# https://spring.io/guides/gs/spring-boot-docker

FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /usr/src/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /usr/src/app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
