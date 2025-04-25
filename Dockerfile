FROM eclipse-temurin:21
COPY ./build/libs/*SNAPSHOT.jar mwd-server.jar
ENTRYPOINT ["java", "-jar", "mwd-server.jar"]