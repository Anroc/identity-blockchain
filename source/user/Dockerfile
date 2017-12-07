# Dockerfile

FROM williamyeh/java8

MAINTAINER  Marvin Petzolt <m.petzolt@campus.tu-berlin.de>

RUN mkdir /opt/Core-Logic-User
ADD build/libs/*.jar /opt/Core-Logic-User/
COPY docker/resources/UnlimitedJCEPolicyJDK8/* /usr/lib/jvm/java-8-oracle/jre/lib/security/

ENTRYPOINT java -jar /opt/Core-Logic-User/*.jar

EXPOSE 8080
