
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
    id("signing")
}

android {
//    namespace = "com.fahmi.authui"
    namespace = "io.github.fahmihidayah.auth.api"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)
    implementation(libs.okhttp.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

group = "io.github.fahmihidayah"
version = "0.1.0"

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.fahmihidayah"
            artifactId = "authui"
            version = "0.1.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("AuthUI")
                description.set("Android authentication UI library with Jetpack Compose and Retrofit")
                url.set("https://github.com/fahmihidayah/authui")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
                    connection.set("scm:git:git://github.com/fahmihidayah/authui.git")
                    developerConnection.set("scm:git:ssh://github.com/fahmihidayah/authui.git")
                    url.set("https://github.com/fahmihidayah/authui")
                }
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    // Only sign if credentials are configured
    val canSign = project.hasProperty("signing.keyId") ||
                  project.hasProperty("signing.gnupg.keyName") ||
                  System.getenv("GPG_KEY_ID") != null

    isRequired = canSign

    if (canSign) {
        sign(publishing.publications["release"])
    }
}