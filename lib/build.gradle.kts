
plugins {
  `java-library`
  alias(libs.plugins.paper.userdev)
  `maven-publish`
}

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}


dependencies {
  paperweight.paperDevBundle(libs.versions.paper.dev.bundle)

}

tasks {
  compileJava {
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
    }
  }
}
