plugins {
	java
	application // Плагин для создания исполняемого JAR
	id("org.springframework.boot") version "3.2.5-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.4"
	checkstyle
	jacoco
	id("checkstyle")
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication")
}
//


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
	implementation("org.springframework.boot:spring-boot-starter-security") //Секьюрити - хеш пароля..
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")


	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6") //нужна для null-маппера, чтоб работать с  null, как передаваемыми значнеиями

	testCompileOnly("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

//	implementation("org.springframework.boot:spring-boot-starter")
//	implementation("org.springframework.boot:spring-boot-starter-web")
//	developmentOnly("org.springframework.boot:spring-boot-devtools")
//
//	implementation("org.springframework.boot:spring-boot-starter-jdbc")
//	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
//	implementation("org.springframework.boot:spring-boot-starter-validation")
//
//	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//
//	compileOnly("org.projectlombok:lombok")
//	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
//
//	implementation("org.mapstruct:mapstruct:1.5.5.Final")
//	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
//
//	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
//
//	runtimeOnly("com.h2database:h2")
//	runtimeOnly("org.postgresql:postgresql")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//
//	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
//	testImplementation("org.springframework.security:spring-security-test")
//	testImplementation(platform("org.junit:junit-bom:5.10.0"))
//	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
//	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
//	implementation("net.datafaker:datafaker:2.0.1")
//	implementation("org.instancio:instancio-junit:3.3.0")
//
//	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }