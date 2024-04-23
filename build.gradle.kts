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
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("net.sf.ehcache:ehcache:2.10.9.2")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("io.jsonwebtoken:jjwt:0.12.5")
    implementation("org.webjars:bootstrap:5.3.3")
    implementation("org.flywaydb:flyway-core:10.11.1")
    implementation("org.flywaydb:flyway-mysql:10.11.1")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.2.3")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:6.2.3")
//    testImplementation("junit:junit:4.13.1")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5")

    implementation("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.2.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    val buildJarProd by creating(Jar::class) {
        val resourcesDir = "src/main/resources"
        from(resourcesDir) {
            include("application-prod.properties")
        }
        archiveFileName.set("HH-prod.jar")
        destinationDirectory.set(layout.buildDirectory.dir("jars/prod"))
    }

    val buildJarLocal by creating(Jar::class) {
        val resourcesDir = "src/main/resources"
        from(resourcesDir) {
            include("application.properties")
        }
        archiveFileName.set("HH-local.jar")
        destinationDirectory.set(layout.buildDirectory.dir("jars/local"))
    }

}
