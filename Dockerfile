FROM eclipse-temurin:19-jre-alpine

COPY build/libs/*.jar /opt/app/

EXPOSE 8080

CMD java -jar /opt/app/application.jar