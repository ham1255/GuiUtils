import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  alias(libs.plugins.paper.userdev)
  id("xyz.jpenilla.run-paper") version "3.0.2"
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.3.0"
  id("com.gradleup.shadow") version "8.3.9"
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}


dependencies {
  paperweight.paperDevBundle(libs.versions.paper.dev.bundle)
  api(project(":GuiUtils-lib"))

}

tasks {
  compileJava {
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

bukkitPluginYaml {
  main = "net.glomc.utils.gui.TestPlugin"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Author")
  apiVersion = "1.21.10"
}
