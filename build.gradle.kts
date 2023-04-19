plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

defaultTasks("shadowJar")

group = "net.analyse"
version = "2.0.0"

plugins.withId("java") {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.github.johnrengelman.shadow")

    // Add the following block

    tasks.shadowJar {
        archiveFileName.set("${project.name}-analyse-${rootProject.version}.jar")
    }

    repositories {
        mavenCentral()
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven {
            url = uri("https://repo.opencollab.dev/main/")
        }
        maven {
            url = uri("https://nexus.velocitypowered.com/repository/maven-public/")
        }
        maven {
            url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        }
    }

    tasks.processResources {
        filteringCharset = "UTF-8"
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        filesNotMatching("**/*.zip") {
            expand("version" to rootProject.version)
        }
    }
}
