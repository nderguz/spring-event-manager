FROM alpine/java:22-jre as build
WORKDIR /opt/app

EXPOSE 9595
COPY target/event-manager-0.0.1-SNAPSHOT.jar event-manager.jar
CMD ["java", "-jar", "event-manager.jar"]
