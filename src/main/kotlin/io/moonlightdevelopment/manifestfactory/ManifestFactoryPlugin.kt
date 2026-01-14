package io.moonlightdevelopment.manifestfactory

import io.moonlightdevelopment.manifestfactory.dsl.HytaleManifestExtension
import io.moonlightdevelopment.manifestfactory.task.GenerateHytaleManifestTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class ManifestFactoryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val ext = project.extensions.create("hytaleManifest", HytaleManifestExtension::class.java)

        ext.group.convention(
            project.provider { project.group.toString() }.map { if (it == "unspecified") "" else it }
        )
        ext.name.convention(project.provider { project.name })
        ext.version.convention(
            project.provider { project.version.toString() }.map { if (it == "unspecified") "" else it }
        )

        val gen = project.tasks.register("generateHytaleManifest", GenerateHytaleManifestTask::class.java) { t ->
            t.outputFile.set(project.layout.buildDirectory.file("generated/manifestfactory/manifest.json"))

            t.manifestGroup.set(ext.group)
            t.manifestName.set(ext.name)
            t.version.set(ext.version)
            t.manifestDescription.set(ext.description)

            t.website.set(ext.website)
            t.main.set(ext.main)
            t.serverVersion.set(ext.serverVersion)

            t.authors.set(ext.authorsList())

            t.dependencies.set(ext.requiredDeps())
            t.optionalDependencies.set(ext.optionalDeps())
            t.loadBefore.set(ext.loadBeforeDeps())

            t.disabledByDefault.set(ext.disabledByDefault)
            t.includesAssetPack.set(ext.includesAssetPack)
        }

        project.plugins.withId("java") {
            val sourceSets = project.extensions.getByType(org.gradle.api.tasks.SourceSetContainer::class.java)
            sourceSets.named("main") {
                it.resources.srcDir(project.layout.buildDirectory.dir("generated/manifestfactory"))
            }
            project.tasks.named("processResources").configure { it.dependsOn(gen) }
        }

        project.tasks.matching { it.name == "processResources" }.configureEach {
            it.dependsOn(gen)
        }
    }
}