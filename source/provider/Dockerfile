# Dockerfile

FROM williamyeh/java8

MAINTAINER  Marvin Petzolt <m.petzolt@campus.tu-berlin.de>

ARG ACTIVE_PROFILE=production-government

RUN mkdir /opt/Core-Logic-Provider
ADD build/libs/*.jar /opt/Core-Logic-Provider/
COPY docker/resources/UnlimitedJCEPolicyJDK8/* /usr/lib/jvm/java-8-oracle/jre/lib/security/
RUN mkdir -p /root/.ethereum/blockchain-identity
COPY docker/resources/goverment/gov-wallet.json /root/.ethereum/blockchain-identity/
RUN if [ "$ACTIVE_PROFILE" != "production-government" ]; then rm /root/.ethereum/blockchain-identity/gov-wallet.json; fi
ENV SPRING_PROFILES_ACTIVE=$ACTIVE_PROFILE

ENTRYPOINT java -jar /opt/Core-Logic-Provider/*.jar

EXPOSE 8080
