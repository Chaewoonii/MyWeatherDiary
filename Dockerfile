FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY ./build/libs/*SNAPSHOT.jar mwd-server.jar

ENTRYPOINT ["java", "-jar", "mwd-server.jar"]