FROM postgres

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=448880Gleb
ENV POSTGRES_DB=ToriApiDb

EXPOSE 5432

FROM maven:3-openjdk-17 as build
COPY src /home/application/src
COPY pom.xml /home/application
COPY mvnw /home/application
COPY mvnw.cmd /home/application
USER root

RUN mkdir -p /home/application && \
    mvn -f /home/application/pom.xml clean package

FROM openjdk:17
COPY --from=build /home/application/target/ToriApi-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
WORKDIR /home/application
CMD ["java", "-jar", "/usr/local/lib/app.jar"]
EXPOSE 8081
