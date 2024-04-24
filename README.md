# Environmental Variables

## PostgreSQL Database Configuration

- `POSTGRESDB_USER`: The username for accessing the PostgreSQL database. Default value: `postgres`.
- `POSTGRESDB_ROOT_PASSWORD`: The password for the root user of the PostgreSQL database. Default value: `123456`.
- `POSTGRESDB_DATABASE`: The name of the PostgreSQL database to be used. Default value: `highhope`.
- `POSTGRESDB_LOCAL_PORT`: The local port number to access the PostgreSQL database. Default value: `5433`.
- `POSTGRESDB_DOCKER_PORT`: The port number used by the PostgreSQL Docker container. Default value: `5432`.

## Spring Application Configuration

- `SPRING_LOCAL_PORT`: The local port number to access the Spring application. Default value: `8080`.
- `SPRING_DOCKER_PORT`: The port number used by the Spring application Docker container. Default value: `8080`.

# Creating Production Application Image

java -jar High-Hopes-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod   

# Deploying application + DB into containers

docker-compose up

# Removing containers

docker-compose down