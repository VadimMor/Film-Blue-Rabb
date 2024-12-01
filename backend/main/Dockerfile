FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/blue-rabb-0.0.1-SNAPSHOT.jar /app/blue-rabb-0.0.1-snapshot.jar
COPY src/main/resources/logback-spring.xml /app/logback-spring.xml

ENTRYPOINT ["java", "-jar", "blue-rabb-0.0.1-snapshot.jar", "--spring.main.banner-mode=off"]