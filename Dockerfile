FROM  openjdk:22-jdk-slim
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -DskipTests

EXPOSE 8080
CMD ["java", "-jar", "target/event-manager-0.0.1-SNAPSHOT.jar"]