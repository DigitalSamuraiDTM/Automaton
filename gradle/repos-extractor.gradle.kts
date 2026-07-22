val pluginsRepos by extra(
    { pluginsManagement: org.gradle.plugin.management.PluginManagementSpec ->

        pluginsManagement.repositories {
            mavenCentral()
            google()
            gradlePluginPortal()
        }
    }
)

val depsRepos by extra(
    { depsRepsResolutionManagement: org.gradle.api.initialization.resolve.DependencyResolutionManagement ->
        depsRepsResolutionManagement.repositories {
            mavenCentral()
            google()
            gradlePluginPortal()
        }
    }
)