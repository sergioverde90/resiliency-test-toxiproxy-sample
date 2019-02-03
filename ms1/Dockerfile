FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/ms1*.jar ms1.jar
CMD java ${JAVA_OPTS} -jar ms1.jar