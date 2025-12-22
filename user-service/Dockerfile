#FROM eclipse-temurin:17
#LABEL maintainer="vatsshubham87@gmail.com"
#WORKDIR /app
#COPY target/user-service-1.0-SNAPSHOT.jar /app/user-service-docker.jar
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "user-service-docker.jar"]


FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# parent pom from root of context
COPY ./pom.xml .

# copy all module poms + src
COPY ./api-gateway ./api-gateway
COPY ./service-discovery ./service-discovery
COPY ./user-service ./user-service
COPY ./product-service ./product-service
COPY ./order-service ./order-service
COPY ./payment-service ./payment-service
COPY ./auth-service ./auth-service

RUN mvn clean package -pl user-service -am

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service/target/user-service-1.0-SNAPSHOT.jar user-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "user-service.jar"]



