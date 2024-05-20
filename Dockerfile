#
# Build stage
#
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . /app/
RUN gradle clean build --no-daemon

#
# Package stage
#
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 7050
ENTRYPOINT ["java","-jar","app.jar"]
