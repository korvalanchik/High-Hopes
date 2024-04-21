plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("net.sf.ehcache:ehcache:2.10.9.2")
//    implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("io.jsonwebtoken:jjwt:0.12.5")
    implementation("org.webjars:bootstrap:5.3.3")
    implementation("org.flywaydb:flyway-core")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.2.3")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:6.2.3")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.2.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    implementation("org.springdoc:springdoc-openapi-ui:1.8.0") // Dependency for OpenApi 3.0
    implementation("org.postgresql:postgresql") // PostgreSQL JDBC driver
    testImplementation("org.mockito:mockito-core:3.12.4") // Dependency for Mockito


}

tasks.withType<Test> {
    useJUnitPlatform()
}

