plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
}

group = "com.sushobh"
version = "2.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(gradleApi())
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("com.android.tools.build:gradle:8.9.1")
}

gradlePlugin {

    plugins {
        create("loggerPlugin") {
            id = "loggerplugin"
            displayName = "ViewModelMethodCallLogger"
            description = "A Gradle plugin to log viewmodel method calls"
            implementationClass = "com.sushobh.loggerplugin.ViewModelTransformerPlugin"
        }
    }
}
publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("D://mavenRepoLocal")
        }
    }
}
