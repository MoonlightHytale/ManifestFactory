plugins {
    alias(libs.plugins.kotlin)
    `java-gradle-plugin`
    alias(libs.plugins.publish)
    alias(libs.plugins.serialization)
}

group = "io.moonlightdevelopment"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.serialization)
}

gradlePlugin {
    website.set("https://moonlightdevelopment.io")
    vcsUrl.set("https://github.com/MoonlightHytale")
    plugins {
        create("manifestFactory") {
            id = "io.moonlightdevelopment.manifestfactory"
            implementationClass = "io.moonlightdevelopment.manifestfactory.ManifestFactoryPlugin"
            displayName = "ManifestFactory"
            description = "Generates Hytale manifest.json from a Kotlin DSL and packages it into the jar."
            tags.set(listOf("hytale", "manifest", "generator", "moonlight"))
        }
    }
}