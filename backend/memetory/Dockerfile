FROM openjdk:17-oracle

COPY build/libs/memetory-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles/active=dev", "-jar", "app.jar"]