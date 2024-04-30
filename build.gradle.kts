plugins {
	application // Плагин для создания исполняемого JAR
	id("org.springframework.boot") version "3.2.5-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.4"
	checkstyle
	jacoco
	id("checkstyle")

	id ("com.github.johnrengelman.processes") version "0.5.0"
	id("org.springdoc.openapi-gradle-plugin") version "1.8.0"

	id ("io.sentry.jvm.gradle") version "4.4.1" //для работы Sentry


}
sentry {
	// Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
	// This enables source context, allowing you to see your source
	// code as part of your stack traces in Sentry.
	includeSourceContext = true

	org = "ivan-3c"
	projectName = "java-spring-boot"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication")
}


buildscript {
	repositories {
		mavenCentral()
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	implementation("org.mapstruct:mapstruct:1.5.5.Final") //для создания маппера по преобразованию сущности в ДТО
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-validation") //нужна для разного рода валидаций полей сущностей
	implementation("org.springframework.boot:spring-boot-starter-security") //Секьюрити/хеш пароля..
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.8.0")//для подключение Sentry для стороннего отлова багов


	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6") //нужна для null-маппера, чтоб работать с  null, как передаваемыми значнеиями

	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0") //для генерации док-ции по API проекта
//	implementation ("org.springdoc:springdoc-openapi-ui:2.5.0") //для генерации док-ции по API проекта

	testCompileOnly("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2") //для контроля ответа в формате-json

	implementation("net.datafaker:datafaker:2.0.1")
	implementation("org.instancio:instancio-junit:3.3.0")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.sentryBundleSourcesJava {
	enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}
tasks.jacocoTestReport { reports { xml.required.set(true) } }