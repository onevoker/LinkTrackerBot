FROM maven:latest AS builder
WORKDIR /app
COPY . .
RUN mvn -pl scrapper -am package

FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/scrapper/target/scrapper.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
