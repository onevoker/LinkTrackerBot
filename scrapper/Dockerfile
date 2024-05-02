FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
COPY ../pom.xml .
RUN mvn -pl scrapper -am package

FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/target/scrapper.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
