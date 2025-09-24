plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.pavelshe11"
version = "0.0.1-SNAPSHOT"
description = "MessengerMicro"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Для работы с бд
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Для работы с сессиями бд
    implementation("org.springframework.session:spring-session-jdbc")
    // Для аутентификации и авторизации
//    implementation ("org.springframework.boot:spring-boot-starter-security")
//    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    // Сваггер документация
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok")
    compileOnly ("org.apache.tomcat:annotations-api:6.0.53")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly ("io.grpc:grpc-netty-shaded:1.73.0")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
