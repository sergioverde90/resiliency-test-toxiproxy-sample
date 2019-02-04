FROM openjdk:13-jdk-alpine3.8
RUN apk --no-cache add curl
COPY target/ms2*.jar ms2.jar
CMD java ${JAVA_OPTS} -jar ms2.jar