.DEFAULT_GOAL := build-run

clean:
	./gradlew clean

build:
	./gradlew clean build

start:
	./gradlew bootRun

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

installDist:
	./gradlew installDist

start-dist:
	./build/install/app/bin/app

lint:
	gradle checkstyleMain checkstyleTest

test:
	gradle test

reload-classes:
	./gradlew -t classes

report:
	gradle jacocoTestReport

check-java-deps:
	./gradlew dependencyUpdates -Drevision=release

.PHONY: build