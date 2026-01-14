package io.moonlightdevelopment.manifestfactory.task

import io.moonlightdevelopment.manifestfactory.dsl.AuthorSpec
import io.moonlightdevelopment.manifestfactory.model.AuthorJson
import io.moonlightdevelopment.manifestfactory.model.ManifestJson
import io.moonlightdevelopment.manifestfactory.spec.AuthorSnapshot
import io.moonlightdevelopment.manifestfactory.spec.ManifestSpec
import io.moonlightdevelopment.manifestfactory.validation.validateManifest
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*


@CacheableTask
abstract class GenerateHytaleManifestTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input abstract val manifestGroup: Property<String>
    @get:Input abstract val manifestName: Property<String>
    @get:Input abstract val version: Property<String>
    @get:Input abstract val manifestDescription: Property<String>

    @get:Input abstract val website: Property<String>
    @get:Input abstract val main: Property<String>
    @get:Input abstract val serverVersion: Property<String>

    @get:Nested
    abstract val authors: ListProperty<AuthorSpec>

    @get:Input abstract val dependencies: MapProperty<String, String>
    @get:Input abstract val optionalDependencies: MapProperty<String, String>
    @get:Input abstract val loadBefore: MapProperty<String, String>

    @get:Input abstract val disabledByDefault: Property<Boolean>
    @get:Input abstract val includesAssetPack: Property<Boolean>

    private val json = Json {
        prettyPrint = true
        prettyPrintIndent = "    "
        encodeDefaults = true
        explicitNulls = false
    }

    @TaskAction
    fun generate() {
        val spec = ManifestSpec(
            group = manifestGroup.orNull,
            name = manifestName.orNull,
            version = version.orNull,
            description = manifestDescription.orNull,
            website = website.orNull,
            main = main.orNull,
            serverVersion = serverVersion.orNull,
            authors = authors.getOrElse(emptyList()).map {
                AuthorSnapshot(
                    name = it.name.orNull,
                    email = it.email.orNull,
                    url = it.url.orNull
                )
            },
            dependencies = dependencies.getOrElse(emptyMap()),
            optionalDependencies = optionalDependencies.getOrElse(emptyMap()),
            loadBefore = loadBefore.getOrElse(emptyMap()),
            disabledByDefault = disabledByDefault.orNull,
            includesAssetPack = includesAssetPack.orNull
        )

        val errors = validateManifest(spec)
        if (errors.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Invalid hytaleManifest configuration:")
                    errors.forEach { appendLine(" - $it") }
                }.trimEnd()
            )
        }

        val model = ManifestJson(
            group = spec.group!!.trim(),
            name = spec.name!!.trim(),
            version = spec.version!!.trim(),
            description = spec.description?.trim().orEmpty(),
            authors = spec.authors.map { a ->
                AuthorJson(
                    name = a.name!!.trim(),
                    email = a.email?.trim().orEmpty(),
                    url = a.url?.trim().orEmpty()
                )
            },
            website = spec.website?.trim().orEmpty(),
            main = spec.main!!.trim(),
            serverVersion = spec.serverVersion?.trim().orEmpty(),
            dependencies = spec.dependencies,
            optionalDependencies = spec.optionalDependencies,
            loadBefore = spec.loadBefore,
            disabledByDefault = spec.disabledByDefault ?: false,
            includesAssetPack = spec.includesAssetPack ?: false
        )

        val json = json.encodeToString(model)

        val out = outputFile.get().asFile
        out.parentFile.mkdirs()
        out.writeText(json)
    }
}