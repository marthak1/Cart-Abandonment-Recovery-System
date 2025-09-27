# Dockerfile for Spring Boot Backend
FROM openjdk:17-jdk-alpine AS build
WORKDIR /app
COPY target/ecommerce-backend-0.0.1-SNAPSHOT.jar app.jar
COPY . .
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]
# RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
