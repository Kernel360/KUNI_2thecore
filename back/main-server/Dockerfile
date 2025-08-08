#FROM gradle:8.4.0-jdk17 AS build
#
#WORKDIR /app
#
## gradle wrapper 포함 전체 복사
#COPY . .
#
## gradlew 실행 권한 부여
#RUN chmod +x ./gradlew
#
## main-server 빌드 (test 제외)
#RUN ./gradlew :main-server:bootJar -x test
#
#FROM eclipse-temurin:17-jdk
#
#WORKDIR /app
#
#COPY --from=build /app/main-server/build/libs/*.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]