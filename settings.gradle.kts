@file:Suppress("UnstableApiUsage")
rootProject.name = "GuiUtils"
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
fun configureProject(name: String, path: String) {
    include(name)
    project(name).projectDir = file(path)
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

configureProject(":GuiUtils-lib", "lib")
configureProject(":GuiUtils-testplugin", "testplugin")
