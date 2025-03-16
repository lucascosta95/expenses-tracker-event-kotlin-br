# Stage 1: Build Stage
FROM eclipse-temurin:21-jdk AS build-stage

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

# Copy source code and build the application
COPY src ./src
RUN mvn -B package -DskipTests

# Stage 2: Runtime Stage
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Install ping and nslookup
RUN apk update && apk add --no-cache iputils bind-tools

# Copy application jar from builder stage
COPY --from=build-stage /app/target/expenses-tracker-backend.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]