import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.0"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.8.0"

	kotlin("jvm") version "1.8.21"
	kotlin("plugin.spring") version "1.8.21"
	kotlin("kapt") version "1.8.21"
//	id("io.freefair.lombok") version "5.3.0"
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

group = "kr.dohoonkim"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring boot
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
//	implementation("org.springframework.boot:spring-boot-starter-websocket")

	// Utilities
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.google.guava:guava:32.0.0-jre")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("commons-io:commons-io:2.12.0")
	runtimeOnly("org.springdoc:springdoc-openapi-kotlin:1.7.0")

	// JWT
	implementation("com.auth0:java-jwt:4.3.0")

	// Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.1.0")

	// QueryDSL
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	implementation("com.querydsl:querydsl-core:5.0.0")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-kotlin-codegen:5.0.0")
	kapt("org.springframework.boot:spring-boot-configuration-processor")
//	kapt ("jakarta.annotation:jakarta.annotation-api")
//	kapt ("jakarta.persistence:jakarta.persistence-api")
	// Database
//	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("com.h2database:h2:2.1.214")
	implementation("org.postgresql:postgresql:42.6.0")
	testRuntimeOnly("org.springdoc:springdoc-openapi-kotlin:1.7.0")
	testImplementation("io.mockk:mockk:1.13.5")
	testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	testImplementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.h2database:h2:2.1.214")
	testImplementation("io.kotest:kotest-runner-junit5:4.4.3")
	testImplementation("io.kotest:kotest-assertions-core:4.4.3")
	testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
	testImplementation("it.ozimov:embedded-redis:0.7.3") { exclude("org.slf4j","slf4j-simple") }

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
