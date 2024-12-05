plugins {
	application
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.github.ben-manes.versions") version "0.50.0"
	id("io.freefair.lombok") version "8.6"
	id("io.sentry.jvm.gradle") version "4.14.1"
}

buildscript {
	repositories {
		mavenCentral()
	}
}

sentry {
	includeSourceContext = true
	org = "daria-p6"
	projectName = "java-spring-boot"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}
tasks.sentryBundleSourcesJava {
	enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication") }

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("com.h2database:h2")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.springframework.boot:spring-boot-starter-security:3.3.4")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("net.datafaker:datafaker:2.4.0")
	implementation("org.instancio:instancio-junit:5.0.1")
	testImplementation(platform("org.junit:junit-bom:5.10.3"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.7")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("io.sentry:sentry-spring:7.18.1")
	implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.18.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
