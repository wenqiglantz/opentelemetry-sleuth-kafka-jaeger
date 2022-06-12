FROM amazoncorretto:17-alpine3.15-jdk

# Datadog agent installation
#RUN apk --no-cache add curl && curl -s -L -o /dd-java-agent.jar "https://dtdg.co/latest-java-tracer"

RUN mkdir -m777 /etc/customer-service
COPY postgre.jks /tmp/postgre.jks
WORKDIR /etc/customer-service
EXPOSE 8081

ADD target/customer-service-0.0.1-SNAPSHOT.jar /etc/customer-service
ENTRYPOINT ["java", "-jar", "/etc/customer-service/customer-service-0.0.1-SNAPSHOT.jar"]
