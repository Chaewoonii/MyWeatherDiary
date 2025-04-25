FROM eclipse-temurin:21
COPY /home/ubuntu/mwd-server/build/libs/*SNAPSHOT.jar mwd-server.jar
ENTRYPOINT ["java", "-jar", "mwd-server.jar"]