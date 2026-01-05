plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("maven-publish")
    id("signing")
    id("com.gradleup.nmcp") version "1.4.3"
}

group = "io.github.fahmihidayah"
version = "0.1.0"

android {
    namespace = "io.github.fahmihidayah.authui"

    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.core:core-ktx:1.15.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                artifactId = "authui"

                pom {
                    name.set("AuthUI")
                    description.set("Android authentication UI library")
                    url.set("https://github.com/fahmihidayah/authui")

                    licenses {
                        license {
                            name.set("Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("fahmihidayah")
                            name.set("Fahmi Hidayah")
                            email.set("m.fahmi.hidayah@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:https://github.com/fahmihidayah/authui.git")
                        developerConnection.set("scm:git:ssh://git@github.com:fahmihidayah/authui.git")
                        url.set("https://github.com/fahmihidayah/authui")
                    }
                }
            }
        }

//        repositories {
//            maven {
//                name = "ossrh"
//                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//                credentials {
////                    username = findProperty("ossrhUsername") as String? ?: ""
//                    username = "4V1Kpf"
//                    password = "hardcodedpassword"
////                    password = findProperty("ossrhPassword") as String? ?: ""
//                }
//            }
//        }
    }

}
signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("GPG_PASSPHRASE")

    if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

nmcp {
    publishAllPublicationsToCentralPortal {
        username.set(providers.gradleProperty("centralPortalUsername"))
        password.set(providers.gradleProperty("centralPortalPassword"))

    }

//    publish {
//        username.set(providers.gradleProperty("centralUsername"))
//        password.set(providers.gradleProperty("centralPassword"))
//
//        publicationType.set("AUTOMATIC")
//    }
}