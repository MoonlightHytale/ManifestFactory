package io.moonlightdevelopment.manifestfactory.spec

data class ManifestSpec(
    val group: String?,
    val name: String?,
    val version: String?,
    val description: String?,

    val website: String?,
    val main: String?,
    val serverVersion: String?,

    val authors: List<AuthorSnapshot>,

    val dependencies: Map<String, String>,
    val optionalDependencies: Map<String, String>,
    val loadBefore: Map<String, String>,

    val disabledByDefault: Boolean?,
    val includesAssetPack: Boolean?
)