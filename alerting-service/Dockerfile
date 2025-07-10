# Stage 1: Build the Quarkus app
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app

# Copy everything inside alerting-service (since this is the context)
COPY . .

RUN mvn package -DskipTests

# Stage 2: Run it
FROM amazoncorretto:21
WORKDIR /app

COPY --from=build /app/target/quarkus-app/ ./

EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
