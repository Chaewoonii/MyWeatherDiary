# Step 1: 빌드 환경 (빌드를 위한 Gradle 환경)
FROM gradle:7.4-jdk11 as build

# 작업 디렉터리 생성
WORKDIR /app

# gradle 파일들을 복사하고 의존성 다운로드
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew build --no-daemon

# Step 2: 실제 실행 환경 (최종 실행을 위한 JDK 환경)
FROM openjdk:11-jre-slim

WORKDIR /app

# 빌드된 .jar 파일을 복사
COPY --from=build /app/build/libs/*.jar mwd-server.jar

# 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "mwd-server.jar"]