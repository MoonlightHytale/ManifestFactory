package io.moonlightdevelopment.manifestfactory.validation

import io.moonlightdevelopment.manifestfactory.spec.ManifestSpec
import java.net.URI

fun validateManifest(spec: ManifestSpec): List<String> {
    val errors = mutableListOf<String>()

    fun req(field: String, v: String?) {
        if (v == null || v.trim().isEmpty()) errors += "$field is required"
    }

    req("Group", spec.group)
    req("Name", spec.name)
    req("Version", spec.version)
    req("Main", spec.main)

    spec.group?.let { g ->
        val s = g.trim()
        if (s.any { it.isISOControl() }) errors += "Group must not contain control characters"
        if (s.contains(' ')) errors += "Group must not contain spaces"
        if (s.contains(':')) errors += "Group must not contain ':'"
    }

    spec.name?.let { n ->
        val s = n.trim()
        if (s.any { it.isISOControl() }) errors += "Name must not contain control characters"
    }

    spec.version?.let { v ->
        val s = v.trim()
        if (s.any { it.isISOControl() }) errors += "Version must not contain control characters"
    }

    spec.main?.let { m ->
        val s = m.trim()
        if (!looksLikeFqcn(s)) errors += "Main must be a fully qualified class name, e.g. com.example.MyPlugin"
    }

    spec.website?.let { w ->
        val s = w.trim()
        if (s.isNotEmpty() && !isHttpUrl(s)) errors += "Website must be a valid http/https URL"
    }

    spec.authors.forEachIndexed { idx, a ->
        val prefix = "Authors[$idx]"
        if (a.name == null || a.name.trim().isEmpty()) errors += "$prefix.Name is required"

        a.email?.trim()?.takeIf { it.isNotEmpty() }?.let { email ->
            if (!looksLikeEmail(email)) errors += "$prefix.Email is not a valid email"
        }

        a.url?.trim()?.takeIf { it.isNotEmpty() }?.let { url ->
            if (!isHttpUrl(url)) errors += "$prefix.Url must be a valid http/https URL"
        }
    }

    validateDepBlock("Dependencies", spec.dependencies, errors)
    validateDepBlock("OptionalDependencies", spec.optionalDependencies, errors)
    validateDepBlock("LoadBefore", spec.loadBefore, errors)

    return errors
}

private fun validateDepBlock(
    label: String,
    map: Map<String, String>,
    errors: MutableList<String>
) {
    val idRegex = Regex("""^[^\s:]+:[^\s:]+$""")
    map.forEach { (k, v) ->
        val key = k.trim()
        val ver = v.trim()

        if (!idRegex.matches(key)) errors += "$label key '$k' must look like 'Company:Plugin'"
        if (ver.isEmpty()) errors += "$label value for '$k' must not be blank"
    }
}

private fun looksLikeFqcn(s: String): Boolean {
    if (s.isBlank()) return false
    if (s.contains(' ') || s.contains('/') || s.contains('\\')) return false
    val parts = s.split('.')
    if (parts.size < 2) return false
    val ident = Regex("""^[A-Za-z_][A-Za-z0-9_]*$""")
    return parts.all { ident.matches(it) }
}

private fun isHttpUrl(s: String): Boolean {
    return try {
        val uri = URI(s)
        (uri.scheme == "http" || uri.scheme == "https") && !uri.host.isNullOrBlank()
    } catch (_: Exception) {
        false
    }
}

private fun looksLikeEmail(s: String): Boolean {
    val r = Regex("""^[^\s@]+@[^\s@]+\.[^\s@]+$""")
    return r.matches(s)
}