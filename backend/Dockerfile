# /project-root/backend/Dockerfile
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY .env .env
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar ./backend.jar
COPY .env .env
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "backend.jar"]