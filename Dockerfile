FROM gradle:8-jdk17 AS builder

WORKDIR /app

COPY build.gradle .
COPY gradlew .
COPY gradle ./gradle
RUN gradle --no-daemon

COPY src ./src

RUN gradle build --no-daemon

FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]