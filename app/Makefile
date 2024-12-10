build:
	./gradlew clean build

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

update:
	./gradlew dependencyUpdates

.PHONY: build

setup:
	npm install
	./gradlew wrapper --gradle-version 8.7
	./gradlew build

frontend:
	make -C frontend start

backend:
	./gradlew bootRun --args='--spring.profiles.active=dev'

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

# generate-migrations:
# 	gradle diffChangeLog

# db-migrate:
# 	./gradlew update

.PHONY: build frontend