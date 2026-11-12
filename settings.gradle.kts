@file:Suppress("UnstableApiUsage")

fun includeIfExists(name: String) {
    val filePath = name.substring(1).replace(":", "/")
    val dir = file(filePath)
    if (dir.exists()) {
        include(name)
        project(name).projectDir = dir
    } else {
        println("Module $name not found")
    }
}

rootProject.name = "Automaton"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    apply(from = "gradle/repos-extractor.gradle.kts")
    val pluginsRepos: ((PluginManagementSpec) -> Unit) by extra
    pluginsRepos(this)
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)

    apply(from = "gradle/repos-extractor.gradle.kts")
    val depsRepos: ((DependencyResolutionManagement) -> Unit) by extra
    depsRepos.invoke(this)
}

includeIfExists(":test")
includeIfExists(":api")
includeIfExists(":creator:ast")
includeIfExists(":creator:semantic")
includeIfExists(":creator:api")
includeIfExists(":creator:grammar")

includeIfExists(":creator:backend:api")
includeIfExists(":creator:backend:generator:api")
includeIfExists(":creator:backend:generator:manual")
includeIfExists(":creator:backend:generator:android:kaspresso")

includeIfExists(":creator:backend:integrator:api")
includeIfExists(":creator:backend:integrator:files")

includeIfExists(":creator:parser:api")
includeIfExists(":creator:parser:ll1")

includeIfExists(":creator:frontend:api")
includeIfExists(":creator:frontend:yaml")

includeIfExists(":creator:logger:api")
includeIfExists(":creator:logger:terminal")

includeIfExists(":creator:utils")

includeIfExists(":grabber:api")
includeIfExists(":grabber:representation")
includeIfExists(":grabber:storage:api")
includeIfExists(":grabber:storage:saas:mongo")
includeIfExists(":grabber:frontend:openapi-file")
