FROM alpine/java:22-jre as build
ARG JAR_FILE=target/event-manager-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app

EXPOSE 9595
COPY ${JAR_FILE} event-manager.jar
CMD ["java", "-jar", "event-manager.jar"]