lint:
	./gradlew checkstyleMain

build:
	./gradlew clean build

test:
	./gradlew test

report:
	make -C app report

setup:
	make -C app setup

.PHONY: build
