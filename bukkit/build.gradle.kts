group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":sdk"))
    implementation("it.unimi.dsi:fastutil:8.5.6")

    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("dev.dejvokep:boosted-yaml:1.3")
    compileOnly("me.clip:placeholderapi:2.11.3")
}

tasks.shadowJar {
    configurations = listOf(project.configurations.runtimeClasspath.get())

    relocate("it.unimi", "net.analyse.plugin.libs.fastutil") // Relocate fastutil
    relocate("okhttp3", "net.analyse.plugin.libs.okhttp3") // Relocate okhttp
    relocate("okio", "net.analyse.plugin.libs.okio") // Relocate okio (okhttp dependency)
    relocate("dev.dejvokep.boostedyaml", "net.analyse.plugin.libs.boostedyaml") // Relocate boostedyaml
    relocate("org.jetbrains.annotations", "net.analyse.plugin.libs.jetbrains") // Relocate jetbrains
    relocate("kotlin", "net.analyse.plugin.libs.kotlin") // Relocate jetbrains
    minimize()

//    minimize {
//        exclude(project(":geyser-bridge"))
//        exclude(project(":geyser-common"))
//    }
}
