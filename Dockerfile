FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar discapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "discapp.jar"]