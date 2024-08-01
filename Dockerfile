# Используем официальный образ Maven с JDK 17 для сборки приложения
FROM maven:3.9.7-openjdk-22 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Используем официальный образ OpenJDK 22 для запуска приложения
FROM openjdk:22-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar ./event-manager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/event-manager.jar"]