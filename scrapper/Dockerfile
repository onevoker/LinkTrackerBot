FROM openjdk:21
WORKDIR /app
COPY /scrapper/target/scrapper.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
