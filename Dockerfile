# ─── Build Stage ─────────────────────────────────────────────────────────────
FROM gradle:7.6-jdk17 AS builder
WORKDIR /home/gradle/project

# 캐시 활용: 의존성만 먼저 다운로드
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle --no-daemon dependencies

# 소스 복사 후 패키징 (테스트 제외)
COPY src src
RUN gradle --no-daemon clean build -x test

# ─── Run Stage ────────────────────────────────────────────────────────────────
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 산출물 복사
COPY --from=builder /home/gradle/project/build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
