# Use an official Maven image to build the app
FROM maven:3.8.8-eclipse-temurin-21-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and project files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Run `mvnw dependency:go-offline` to cache dependencies
RUN ./mvnw dependency:go-offline

# Copy the entire project source
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Use an OpenJDK image to run the app
FROM eclipse-temurin:21-jre AS runner

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
