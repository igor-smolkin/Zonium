FROM gradle:8.1.1-jdk17 AS build
COPY . /app
WORKDIR /app
RUN chmod +x gradlew && ./gradlew bootJar

FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/auth-service.jar auth-service.jar
ENTRYPOINT ["java", "-jar", "auth-service.jar"]