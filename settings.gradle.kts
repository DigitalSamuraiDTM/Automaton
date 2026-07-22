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
//    includeBuild("build-logic")
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
includeIfExists(":compiler:api")
includeIfExists(":compiler:compiler")
includeIfExists(":compiler:backend:api")

includeIfExists(":compiler:frontend:api")
includeIfExists(":compiler:frontend:yaml")

