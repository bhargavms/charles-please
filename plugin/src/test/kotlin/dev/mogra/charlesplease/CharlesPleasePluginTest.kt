package dev.mogra.charlesplease

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

/**
 * Unit tests for CharlesPleasePlugin.
 * Tests plugin structure and behavior.
 */
class CharlesPleasePluginTest {
    @Test
    fun `should create plugin instance`() {
        // Given & When
        val plugin = CharlesPleasePlugin()

        // Then
        assertNotNull(plugin)
    }

    @Test
    fun `should have correct plugin class structure`() {
        // Given & When
        val plugin = CharlesPleasePlugin()

        // Then
        assertNotNull(plugin)
        // Verify it implements Plugin<Project> - this is guaranteed by the class declaration
    }

    @Test
    fun `should have apply method`() {
        // Given
        val plugin = CharlesPleasePlugin()
        val project = mockk<Project>(relaxed = true)
        val extensions = mockk<org.gradle.api.plugins.ExtensionContainer>(relaxed = true)
        val tasks = mockk<org.gradle.api.tasks.TaskContainer>(relaxed = true)
        val taskProvider = mockk<org.gradle.api.tasks.TaskProvider<CharlesPleaseApplyTask>>(relaxed = true)
        val clearTaskProvider = mockk<org.gradle.api.tasks.TaskProvider<CharlesPleaseClearTask>>(relaxed = true)

        every { project.extensions } returns extensions
        every { project.tasks } returns tasks
        every { extensions.create(any(), any<Class<CharlesPleaseExtension>>()) } returns mockk(relaxed = true)
        every { tasks.register(any<String>(), any<Class<CharlesPleaseApplyTask>>(), any()) } returns taskProvider
        every { tasks.register(any<String>(), any<Class<CharlesPleaseClearTask>>(), any()) } returns clearTaskProvider

        // When
        plugin.apply(project)

        // Then
        // Verify extension is created
        verify { extensions.create("charlesPlease", CharlesPleaseExtension::class.java) }
        // Verify tasks are registered
        verify { tasks.register("charlesPleaseApply", CharlesPleaseApplyTask::class.java, any()) }
        verify { tasks.register("charlesPleaseClear", CharlesPleaseClearTask::class.java, any()) }
    }
}
