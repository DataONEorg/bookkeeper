FROM java:8-jre
COPY bookkeeper.yml /opt/bookkeeper/
COPY target/bookkeeper-1.0-SNAPSHOT.jar /opt/bookkeeper/
EXPOSE 8080
WORKDIR /opt/bookkeeper
CMD ["java", "-jar", "-Done-jar.silent=true", "bookkeeper-1.0-SNAPSHOT.jar", "server", "bookkeeper.yml"]
