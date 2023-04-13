FROM openjdk:8-jre

LABEL maintainer="DataONE support@dataone.org"

RUN mkdir -p /app/bookkeeper

WORKDIR /app/bookkeeper

# The bookkeeper configuration is obtained from a persistent volume mounted at /opt/local/

COPY target/bookkeeper-0.4.0.jar bookkeeper.jar

EXPOSE 8080 8081

# The bookkeeper configuration is obtained from a persistent volume mounted at /opt/local/
CMD ["java", "-jar", "-Done-jar.silent=true", "bookkeeper.jar", "server", "/opt/local/bookkeeper/bookkeeper.yml"]
