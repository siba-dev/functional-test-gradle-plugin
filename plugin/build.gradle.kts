plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    id("com.gradle.plugin-publish") version "2.0.0"
}

group = "de.siba"
version = "1.0.0"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin Test integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

gradlePlugin {
    website = "https://github.com/siba-dev/functional-test-gradle-plugin"
    vcsUrl = "https://github.com/siba-dev/functional-test-gradle-plugin.git"

    // Define the plugin
    val plugin by plugins.creating {
        id = "$group.functional-test"
        implementationClass = "de.siba.functionaltest.FunctionalTestGradlePlugin"
        displayName = "Functional Test Gradle Plugin"
        description = "Convenience plugin adding functional tests to Gradle plugin projects."
        tags = listOf("testing", "integrationTesting")
    }
}

tasks.withType<Test> {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}