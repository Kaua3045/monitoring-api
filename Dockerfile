FROM eclipse-temurin:19-jre-alpine

WORKDIR /monitoring/api/

COPY build/libs/*.jar /monitoring/api/

EXPOSE 8080

CMD java -javaagent:/home/ubuntu/dd-java-agent.jar \
      -Ddd.logs.injection=true \
      -Ddd.service=monitoring-api \
      -jar ./application.jar