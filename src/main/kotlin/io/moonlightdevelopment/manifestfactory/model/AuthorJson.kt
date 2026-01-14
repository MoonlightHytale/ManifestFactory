package io.moonlightdevelopment.manifestfactory.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorJson(
    @SerialName("Name") val name: String,
    @SerialName("Email") val email: String = "",
    @SerialName("Url") val url: String = "",
)