package io.moonlightdevelopment.manifestfactory.dsl

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import javax.inject.Inject

abstract class DependenciesBlock @Inject constructor(objects: ObjectFactory) {
    val required: MapProperty<String, String> =
        objects.mapProperty(String::class.java, String::class.java)

    val optional: MapProperty<String, String> =
        objects.mapProperty(String::class.java, String::class.java)

    val loadBefore: MapProperty<String, String> =
        objects.mapProperty(String::class.java, String::class.java)

    fun require(id: String, version: String) {
        required.put(id, version)
    }

    fun optional(id: String, version: String) {
        optional.put(id, version)
    }

    fun loadBefore(id: String, version: String) {
        loadBefore.put(id, version)
    }
}