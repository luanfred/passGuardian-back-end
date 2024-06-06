FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/passGuardian-1.0.0.jar passGuardian-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "passGuardian-1.0.0.jar"]
