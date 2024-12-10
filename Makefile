build:
	./gradlew clean build

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

update:
	./gradlew dependencyUpdates

setup:
	npm install
	./gradlew wrapper --gradle-version 8.7
	./gradlew build

clean:
	./gradlew clean

dev:
	heroku local

reload-classes:
	./gradlew -t classes

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

# start-dist:
# 	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

# report:
# 	./gradlew jacocoTestReport

update-js-deps:
	npx ncu -u

check-java-deps:
	./gradlew dependencyUpdates -Drevision=release

.PHONY: build

