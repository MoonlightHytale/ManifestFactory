package io.moonlightdevelopment.manifestfactory.dsl

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import javax.inject.Inject

abstract class AuthorSpec @Inject constructor(objects: ObjectFactory) {
    @get:Input
    abstract val name: Property<String>

    @get:Input
    @get:Optional
    abstract val email: Property<String>

    @get:Input
    @get:Optional
    abstract val url: Property<String>

    init {
        email.convention("")
        url.convention("")
    }
}