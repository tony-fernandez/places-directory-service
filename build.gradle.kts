plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.openapi.generator") version "7.2.0"
	id("com.github.dawnwords.jacoco.badge") version  "0.2.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.apache.commons:commons-collections4:4.1")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/docs/openapi.yaml")
	outputDir.set(layout.buildDirectory.dir("generated").get().asFile.absolutePath)
	apiPackage.set("com.example.places.directory.api")
	modelPackage.set("com.example.places.directory.model")
	configOptions.set(
		mapOf(
			"useSpringBoot3" to "true",
			"useSpringfox" to "false",
			"interfaceOnly" to "true",
			"dateLibrary" to "java8"
		)
	)
}

sourceSets {
	main {
		java {
			srcDir(layout.buildDirectory.dir("generated/src/main/java").get().asFile.absolutePath)
		}
	}
}

tasks.withType<JavaCompile> {
	dependsOn("openApiGenerate")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
