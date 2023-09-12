FROM eclips-temurin:11-alpine
COPY target/project-0.0.1-SNAPSHOT.jar project-1.0.0.jar
ENTRYPOINT ["java","-jar","/project-1.0.0.jar"]