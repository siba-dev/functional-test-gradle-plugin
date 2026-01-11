package de.siba.functionaltest

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class FunctionalTestGradlePluginTest {
    @Test
    fun sourceSetPresent() {
        val project = createProject()

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val developmentExtension = project.extensions.getByType(GradlePluginDevelopmentExtension::class.java)

        assertNotNull(
            javaExtension.sourceSets.findByName("functionalTest"), "Could not find source set."
        )
        assertNotNull(developmentExtension.testSourceSets.find {
            it.name == "functionalTest"
        }, "Test source set not registered.")
    }

    private fun createProject(): Project {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("de.siba.functional-test")
        return project
    }

    @Test
    fun taskRegistered() {
        val project = createProject()

        assertNotNull(
            project.tasks.findByName("functionalTest"), "Functional test task not registered."
        )

        val checkTask = project.tasks.getByName("check")
        assertNotNull(
            checkTask.taskDependencies.getDependencies(checkTask)
                .find { it.name == "functionalTest" })
    }
}
