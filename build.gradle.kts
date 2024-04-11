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
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.h2database:h2")

	compileOnly("org.projectlombok:lombok:1.18.24")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }