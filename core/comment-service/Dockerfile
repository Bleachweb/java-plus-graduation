FROM amazoncorretto:21-alpine
LABEL authors="Bleachweb"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]