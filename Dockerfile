# Only Stage 2: Create the runtime image
FROM openjdk:17-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the previous build stage
COPY /build/libs/High-Hopes-0.0.1-SNAPSHOT.jar /app/highhopes.jar

# Copy the wait-for-it script
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# Run the application when the container starts
CMD ["./wait-for-it.sh", "postgresdb:$POSTGRESDB_DOCKER_PORT", "--", "java", "-jar", "highhopes.jar", "--spring.profiles.active=prod"]