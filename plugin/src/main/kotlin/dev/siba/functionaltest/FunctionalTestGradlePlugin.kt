package dev.siba.functionaltest

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

class FunctionalTestGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("java-gradle-plugin")

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

        // Add a source set for the functional test suite
        val functionalTestSourceSet = javaExtension.sourceSets.create("functionalTest")
        project.configurations.getByName("functionalTestImplementation").extendsFrom(
            project.configurations.getByName("testImplementation")
        )
        project.configurations.getByName("functionalTestRuntimeOnly").extendsFrom(
            project.configurations.getByName("testRuntimeOnly")
        )

        project.extensions.getByType(GradlePluginDevelopmentExtension::class.java).testSourceSets.add(
            functionalTestSourceSet
        )

        // Add a task to run the functional tests
        val functionalTest = project.tasks.register("functionalTest", Test::class.java) { task ->
            task.testClassesDirs = functionalTestSourceSet.output.classesDirs
            task.classpath = functionalTestSourceSet.runtimeClasspath
            task.useJUnitPlatform()
        }

        project.tasks.named("check") { task ->
            // Run the functional tests as part of `check`
            task.dependsOn(functionalTest)
        }
    }
}
