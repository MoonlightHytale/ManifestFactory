# Manifest Factory Gradle Plugin

A Gradle plugin that generates a Hytale `manifest.json` from a type-safe Kotlin DSL and packages it into your jar.

## Features

- Kotlin DSL for defining Hytale plugin metadata
- Validation with clear error messages
- Generates a deterministic, formatted `manifest.json`
- Automatically included in the jar resources
- Uses `kotlinx-serialization` for correctness and maintainability

## Requirements

- Gradle 9.2.1 or newer
- Java 21

## Installation

```kotlin
plugins {
    id("io.moonlightdevelopment.manifestfactory") version "<version>"
}
```

## Basic Usage

```kotlin
hytaleManifest {
    group.set("net.moonlightdevelopment")
    name.set("unify")
    version.set("1.0.0")

    description.set("A Discord chat bridge for Hytale servers.")
    main.set("net.moonlightdevelopment.unify.UnifyPlugin")
    serverVersion.set(">=0.1.0")

    authors {
        author {
            name.set("Simeon")
            email.set("info@moonlightdevelopment.io")
            url.set("https://moonlightdevelopment.io")
        }
    }

    dependencies {
        require("Hytale:CorePlugin", ">=1.0.0")
    }
}
```

## Generated Output

The plugin generates:

```
build/generated/manifestfactory/manifest.json
```

This file is automatically added to the main resources and ends up at the root of the jar.

## Validation Rules

The plugin validates the manifest at build time:

- `group`, `name`, `version`, and `main` are required
- `main` must be a valid fully qualified class name
- Dependency keys must be in the form `Company:Plugin`
- URLs must be valid `http` or `https` URLs
- Author `name` is required; `email` and `url` are optional

Invalid configurations fail the build with a readable error list.

## Design Notes

- The DSL uses Gradle `Property` types for lazy configuration
- Internal task properties avoid name collisions with Gradle’s Task API
- Generated output lives in `build/`, not `.gradle/`

## Recommended `.gitignore`

```gitignore
/build/
run/
.hytale-downloader-credentials.json
```