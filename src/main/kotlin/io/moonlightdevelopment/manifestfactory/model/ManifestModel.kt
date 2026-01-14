package io.moonlightdevelopment.manifestfactory.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManifestJson(
    @SerialName("Group") val group: String,
    @SerialName("Name") val name: String,
    @SerialName("Version") val version: String,
    @SerialName("Description") val description: String = "",
    @SerialName("Authors") val authors: List<AuthorJson> = emptyList(),
    @SerialName("Website") val website: String = "",
    @SerialName("Main") val main: String,
    @SerialName("ServerVersion") val serverVersion: String = "",
    @SerialName("Dependencies") val dependencies: Map<String, String> = emptyMap(),
    @SerialName("OptionalDependencies") val optionalDependencies: Map<String, String> = emptyMap(),
    @SerialName("LoadBefore") val loadBefore: Map<String, String> = emptyMap(),
    @SerialName("DisabledByDefault") val disabledByDefault: Boolean = false,
    @SerialName("IncludesAssetPack") val includesAssetPack: Boolean = false,
)