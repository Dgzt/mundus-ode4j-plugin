/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.3/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("maven-publish")

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven {
        url=uri("https://jitpack.io")
    }
}

dependencies {
    api("com.badlogicgames.gdx:gdx:1.12.0")

//    implementation("com.github.Dgzt.Mundus:commons:plugin-new-component-SNAPSHOT")
//    implementation("com.github.Dgzt.Mundus:plugin-api:plugin-new-component-SNAPSHOT")
//    implementation("com.github.Dgzt.Mundus:editor-commons:plugin-new-component-SNAPSHOT")

    api("com.github.antzGames:gdx-ode4j:master-SNAPSHOT")

    api("com.github.Dgzt.Mundus:commons:terrain-system-updated-with-plugin-custom-asset-SNAPSHOT")

//    implementation("com.github.jamestkhan.mundus:commons:master-SNAPSHOT")
//    implementation("com.github.jamestkhan.mundus:plugin-api:master-SNAPSHOT")
//    implementation("com.github.jamestkhan.mundus:editor-commons:master-SNAPSHOT")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }

    withJavadocJar()
    withSourcesJar()
}


tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.dgzt.mundus.plugin.ode4j"
            artifactId = project.name
            version = "0.0.1"
            from(components["java"])
        }
    }
}
