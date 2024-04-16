# Use the official Java 17 image with OpenJDK
FROM openjdk:17-oracle

# Switch to the /app working directory
WORKDIR /app

# Copy the application JAR file into the container
COPY build/libs/High-Hopes-0.0.1-SNAPSHOT.jar /app/highhopes.jar

# Run the application upon container start
CMD ["java", "-jar", "/app/highhopes.jar", "--spring.profiles.active=prod"]