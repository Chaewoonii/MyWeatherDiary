FROM eclipse-temurin:21
COPY ./build/libs/*SNAPSHOT.jar mwd-app.jar
ENTRYPOINT ["java", "-jar", "mwd-server.jar"]