/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.3/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.9.20"

    kotlin("kapt") version "1.9.20"

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
    api("org.pf4j:pf4j:3.10.0")
    api("com.badlogicgames.gdx:gdx:1.13.0")
    kapt("org.pf4j:pf4j:3.11.0")
    api(project(":runtime"))

    implementation("com.github.jamestkhan.mundus:commons:master-SNAPSHOT")
    implementation("com.github.jamestkhan.mundus:plugin-api:master-SNAPSHOT")
    implementation("com.github.jamestkhan.mundus:editor-commons:master-SNAPSHOT")

    implementation("com.github.quickhull3d:quickhull3d:1.0.0")
    implementation("com.massisframework.j3d:java3d-core:1.6.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<Jar> {
    dependsOn(":runtime:jar")

    archiveFileName.set("ode4j-plugin.jar")

    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Plugin-Class"]= "com.github.dgzt.mundus.plugin.ode4j.MundusOde4jEditorPlugin"
        attributes["Plugin-Id"] = "ode4j-plugin"
        attributes["Plugin-Provider"] = "Tibor Zsuro (Dgzt)"
        attributes["Plugin-Version"] = "0.0.1-SNAPSHOT"
    }

    // Include runtime in jar file
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .filter {
            it.name.equals("runtime.jar") ||
            it.name.equals("gdx-ode4j-master-SNAPSHOT.jar") ||
            it.name.equals("quickhull3d-1.0.0.jar") ||
            it.name.equals("vecmath-1.6.0.1.jar") ||
            it.name.equals("java3d-core-1.6.0.1.jar")
        }
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
