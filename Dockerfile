FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.5

WORKDIR .

COPY . .

RUN ./gradlew --no-daemon dependencies

RUN ./gradlew build

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar

#CMD ./build/install/app/bin/app
