FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.7

WORKDIR /

COPY ./ .

RUN ./gradlew --no-daemon dependencies

RUN ./gradlew --no-daemon build

EXPOSE 8080

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar


#CMD ["java", "-XX:+EnableDynamicAgentLoading", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]

#FROM gradle:8.7-jdk21

#WORKDIR ./

#COPY ./ .

#RUN gradle installDist

#CMD ./build/install/app/bin/app