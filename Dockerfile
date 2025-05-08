FROM maven:latest AS builder
COPY pom.xml /build/pom.xml
COPY src /build/src
WORKDIR /build
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=builder build/target/*.jar /app/my-app.jar
WORKDIR /app
CMD ["java", "-jar", "my-app.jar"]