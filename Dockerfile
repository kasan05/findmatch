FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-23 AS build

# Set the working directory inside the container
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


FROM --platform=$BUILDPLATFORM eclipse-temurin:23-jre-alpine AS runtime
WORKDIR /app
# Copy the application's JAR file into the container
# The JAR file is assumed to be in the 'target/' directory after a 'mvn package' build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
# Expose the port on which the Spring Boot application runs (default is 8080)

# Define the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]