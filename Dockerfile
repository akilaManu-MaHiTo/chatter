# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/chatter-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (optional, for documentation)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
