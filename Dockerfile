FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ./target/notesapi-1.0.0-SNAPSHOT.jar noteapi-1.0.0.jar
ENTRYPOINT ["java","-jar","-Djava.security.egd=file:/dev/./urandom","/noteapi-1.0.0.jar"]