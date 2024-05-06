FROM openjdk:21
WORKDIR /app
COPY /bot/target/bot.jar .
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "bot.jar"]
