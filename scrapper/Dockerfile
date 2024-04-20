FROM openjdk:21
WORKDIR /app
COPY /scrapper/target/bot.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "scrapper.jar"]
