lint:
	./gradlew checkstyleMain

build:
	./gradlew clean build

test:
	./gradlew test

report:
	make -C app report

setup:
	npm install
	./gradlew wrapper --gradle-version 8.7
	./gradlew build

.PHONY: build
