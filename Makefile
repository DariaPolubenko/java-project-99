build:
	./gradlew clean build

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

clean:
	./gradlew clean

install:
	./gradlew installDist

lint:
	./gradlew checkstyleMain checkstyleTest

.PHONY: build

