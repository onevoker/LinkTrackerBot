FROM maven:latest AS builder
WORKDIR /app
COPY pom.xml .
COPY scrapper ./scrapper
RUN mvn -pl scrapper -am package

FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/scrapper/target/scrapper.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
