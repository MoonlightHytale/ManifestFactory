package io.moonlightdevelopment.manifestfactory.dsl

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import javax.inject.Inject

abstract class AuthorsBlock @Inject constructor(
    private val objects: ObjectFactory
) {
    val items: ListProperty<AuthorSpec> = objects.listProperty(AuthorSpec::class.java)

    fun author(action: Action<AuthorSpec>) {
        val a = objects.newInstance(AuthorSpec::class.java)
        action.execute(a)
        items.add(a)
    }
}