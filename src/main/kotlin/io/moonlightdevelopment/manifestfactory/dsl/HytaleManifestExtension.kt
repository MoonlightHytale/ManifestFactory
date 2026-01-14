package io.moonlightdevelopment.manifestfactory.dsl

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import javax.inject.Inject

abstract class HytaleManifestExtension @Inject constructor(
    objects: ObjectFactory
) {
    val group: Property<String> = objects.property(String::class.java)
    val name: Property<String> = objects.property(String::class.java)
    val version: Property<String> = objects.property(String::class.java)
    val description: Property<String> = objects.property(String::class.java)

    val website: Property<String> = objects.property(String::class.java)
    val main: Property<String> = objects.property(String::class.java)
    val serverVersion: Property<String> = objects.property(String::class.java)

    val disabledByDefault: Property<Boolean> = objects.property(Boolean::class.java)
    val includesAssetPack: Property<Boolean> = objects.property(Boolean::class.java)

    private val authorsBlock: AuthorsBlock = objects.newInstance(AuthorsBlock::class.java)
    private val depsBlock: DependenciesBlock = objects.newInstance(DependenciesBlock::class.java)

    fun authors(action: Action<AuthorsBlock>) = action.execute(authorsBlock)
    fun dependencies(action: Action<DependenciesBlock>) = action.execute(depsBlock)

    internal fun authorsList(): ListProperty<AuthorSpec> = authorsBlock.items
    internal fun requiredDeps(): MapProperty<String, String> = depsBlock.required
    internal fun optionalDeps(): MapProperty<String, String> = depsBlock.optional
    internal fun loadBeforeDeps(): MapProperty<String, String> = depsBlock.loadBefore

    init {
        description.convention("")
        website.convention("")
        serverVersion.convention("")
        disabledByDefault.convention(false)
        includesAssetPack.convention(false)
    }
}