FROM openjdk:13-jdk-alpine3.8
RUN apk --no-cache add curl
COPY target/ms1*.jar ms1.jar
CMD java ${JAVA_OPTS} -jar ms1.jar