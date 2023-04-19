plugins {
    id("net.kyori.blossom") version "1.2.0"
}

group = rootProject.group
version = rootProject.version

blossom {
    replaceToken("@VERSION@", version)
}

dependencies {
    implementation(project(":sdk"))
    implementation("net.sf.trove4j:trove4j:3.0.3") // Add trove4j dependency

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
    compileOnly("dev.dejvokep:boosted-yaml:1.3")
}

tasks.shadowJar {
    configurations = listOf(project.configurations.runtimeClasspath.get())

    relocate("gnu.trove4j", "net.analyse.plugin.libs.trove4j") // Relocate trove4j
    relocate("okhttp3", "net.analyse.plugin.libs.okhttp3") // Relocate okhttp
    relocate("okio", "net.analyse.plugin.libs.okio") // Relocate okio (okhttp dependency)
    relocate("dev.dejvokep.boostedyaml", "net.analyse.plugin.libs.boostedyaml") // Relocate boostedyaml
    relocate("org.jetbrains.annotations", "net.analyse.plugin.libs.jetbrains") // Relocate jetbrains
    relocate("kotlin", "net.analyse.plugin.libs.kotlin") // Relocate jetbrains
    minimize()
}
