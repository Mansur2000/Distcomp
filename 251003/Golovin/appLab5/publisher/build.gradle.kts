import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
	id("org.jetbrains.kotlin.jvm")
	id("org.jetbrains.kotlin.plugin.serialization")
	id("io.ktor.plugin")
}

group = "com.example.eger"
version = LocalDate.now().format(DateTimeFormatter.ofPattern("yy.MM.dd"))

dependencies {
	implementation(project(":appLab5:common"))

	implementation("org.postgresql:postgresql:latest.release")
	implementation("com.h2database:h2:latest.release")

	implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
	implementation("io.ktor:ktor-server-content-negotiation-jvm")

	implementation("io.ktor:ktor-server-core-jvm")
	implementation("io.ktor:ktor-server-freemarker-jvm")
	implementation("io.ktor:ktor-server-netty-jvm")
	implementation("io.ktor:ktor-server-double-receive")

	implementation("io.insert-koin:koin-ktor:latest.release")
	implementation("io.insert-koin:koin-logger-slf4j:latest.release")

	implementation("io.ktor:ktor-client-core")
	implementation("io.ktor:ktor-client-cio")
	implementation("io.ktor:ktor-client-logging")
	implementation("io.ktor:ktor-client-content-negotiation")

	implementation("org.apache.kafka:kafka-clients:latest.release")

	implementation("io.lettuce:lettuce-core:latest.release")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(11)
	}
}

application {
	mainClass = "com.example.eger.dc.lab5.PublisherKt"

	val isDevelopment = project.ext.has("development")
	applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}