FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.5

WORKDIR .

COPY . .

RUN ./gradlew --no-daemon dependencies

CMD ./build/install/app/bin/app
