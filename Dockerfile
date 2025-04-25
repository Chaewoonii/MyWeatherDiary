FROM openjdk:21-jdk
COPY ./build/libs/*SNAPSHOT.jar mwd-server.jar
ENTRYPOINT ["java", "-jar", "mwd-server.jar"]