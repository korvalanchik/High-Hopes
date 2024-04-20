# Stage 1: Set up the build environment
FROM gradle:8.7-jdk17 AS builder

# Copy the application source code into the container
COPY . /app

# Set the working directory to /app
WORKDIR /app

# Build the application
RUN gradle build

# Stage 2: Create the runtime image
FROM openjdk:17-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the previous build stage
COPY --from=builder /app/build/libs/High-Hopes-0.0.1-SNAPSHOT.jar /app/highhopes.jar

# Run the application when the container starts
CMD ["java", "-jar", "highhopes.jar", "--spring.profiles.active=prod"]