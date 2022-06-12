FROM amazoncorretto:17-alpine3.15-jdk

# Datadog agent installation
#RUN apk --no-cache add curl && curl -s -L -o /dd-java-agent.jar "https://dtdg.co/latest-java-tracer"

RUN mkdir -m777 /etc/order-service
COPY postgre.jks /tmp/postgre.jks
WORKDIR /etc/order-service
EXPOSE 8081

ADD target/order-service-0.0.1-SNAPSHOT.jar /etc/order-service
ENTRYPOINT ["java", "-jar", "/etc/order-service/order-service-0.0.1-SNAPSHOT.jar"]
