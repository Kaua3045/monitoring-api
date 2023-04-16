FROM eclipse-temurin:19-jre-alpine

COPY build/libs/*.jar /opt/app/

EXPOSE 8080

CMD java -javaagent:/home/ubuntu/dd-java-agent.jar \
      -Ddd.logs.injection=true \
      -Ddd.service=monitoring-api \
      -jar /opt/app/application.jar