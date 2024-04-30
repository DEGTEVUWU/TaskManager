FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.5

RUN apt-get update && apt-get install -yq make unzip

WORKDIR .

COPY . .

RUN ./gradlew --no-daemon dependencies

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar

#CMD ./build/install/app/bin/app
