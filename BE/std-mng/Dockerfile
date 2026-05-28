# 1단계: Maven으로 빌드
FROM maven:3.8.6-openjdk-11 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

# 2단계: 빌드된 WAR을 Tomcat에 올리기
FROM tomcat:9.0-jdk11

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080