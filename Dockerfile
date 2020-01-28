FROM openjdk:8-jre

LABEL maintainer="DataONE support@dataone.org"

RUN mkdir -p /opt/bookkeeper

COPY bookkeeper.yml /opt/bookkeeper/

COPY target/bookkeeper-1.0-SNAPSHOT.jar /opt/bookkeeper/

EXPOSE 8080 8081

WORKDIR /opt/bookkeeper

ENTRYPOINT ["java", "-jar", "-Done-jar.silent=true", "bookkeeper-1.0-SNAPSHOT.jar", "server", "bookkeeper.yml"]
