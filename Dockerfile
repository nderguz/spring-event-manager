FROM maven:3.9.4-eclipse-temurin-17 as build

COPY src src
COPY pom.xml pom.xml
RUN mvn clean package dependency:copy-dependencies -DskipTests -DincludeScope=runtime

FROM bellsoft/liberica-openjdk-debian:22

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app
COPY --from=build target/dependency ./lib
COPY --from=build target/event-manager-0.0.1-SNAPSHOT.jar ./application.jar

ENTRYPOINT ["java", "-cp", "./lib/*:./application.jar", "org.example.eventmanager.EventManagerApplication"]