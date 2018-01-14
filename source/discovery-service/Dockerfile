# Dockerfile

FROM williamyeh/java8

MAINTAINER  Marvin Petzolt <m.petzolt@campus.tu-berlin.de>

RUN mkdir /opt/DiscoveryService
ADD build/libs/*.jar /opt/DiscoveryService/
ENV SPRING_PROFILES_ACTIVE=production

ENTRYPOINT java -jar /opt/DiscoveryService/*.jar

EXPOSE 8080
