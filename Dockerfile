FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY ./build/libs/*SNAPSHOT.jar mwd-server.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "mwd-server.jar"]