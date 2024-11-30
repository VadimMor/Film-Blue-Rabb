FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/blue-rabb-0.0.1-SNAPSHOT.jar /app/blue-rabb-0.0.1-snapshot.jar

ENTRYPOINT ["java", "-jar", "blue-rabb-0.0.1-snapshot.jar"]