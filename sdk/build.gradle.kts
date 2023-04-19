plugins {
    java
    `maven-publish`
    `signing`
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("sdk") {
            from(components["java"])
            artifactId = "sdk"

            pom {
                name.set("Analyse")
                description.set("TODO") // Add description
                url.set("https://github.com/track/plugin/")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                    }
                }

                developers {
                    // add devs
                }

                // Change later
                scm {
                    connection.set("scm:git:git://github.com/track/plugin.git")
                    developerConnection.set("scm:git:ssh://github.com:track/plugin.git")
                    url.set("https://github.com/track/plugin")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
//                username = "${ossrhUsername}"
//                password = "${ossrhPassword}"
            }
        }
    }
}

signing {
    sign(publishing.publications["sdk"])
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("dev.dejvokep:boosted-yaml:1.3")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("com.google.guava:guava:30.1.1-jre")
}
