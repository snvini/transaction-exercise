FROM gradle:7.4.0-jdk17 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon


FROM openjdk:17-jdk-slim
EXPOSE 8081
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/transactions.jar
ENTRYPOINT ["java", "-jar", "/app/transactions.jar"]
