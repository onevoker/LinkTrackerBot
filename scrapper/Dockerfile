FROM maven:latest AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:21
WORKDIR /app
COPY --from=builder /scrapper/target/scrapper.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
