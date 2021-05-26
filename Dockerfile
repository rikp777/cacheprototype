
# First we build the jar with maven in a fat image with all the tools.
FROM maven:3.6.3-openjdk-11 as build
LABEL maintainer="niekvandedonk@hotmail.com"
WORKDIR /app
COPY pom.xml .
# Install maven dependency packages, this takes about a minute.
RUN mvn dependency:go-offline
# Then we copy in all source and build splitting out dependencies and source
# will save time in re-builds.
COPY . .
RUN mvn -T 2C install -DskipTests
# Unpack fat jar.
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM build as development
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]



# This stage starts fresh with a minimal debian image and only copying over
# source code.
FROM openjdk:11-jre-slim as prod


# Don't run as root, create a new `java` user here.
RUN groupadd --gid 1000 java \
  && useradd --uid 1000 --gid java --shell /bin/bash --create-home java
USER java


# Copy over unpacked jar content from build stage.
ARG DEPENDENCY=/app/target/dependency
COPY --from=build --chown=java:java ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build --chown=java:java ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build --chown=java:java ${DEPENDENCY}/BOOT-INF/classes /app
CMD ["java","-cp","app:app/lib/*","-Dspring.devtools.restart.enabled=false", "redis.poc.redisApplication.RedisApplication"]