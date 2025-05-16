plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `kotlin-dsl`
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
gradlePlugin {
    plugins {
        create("loggerplugin") {
            id = "com.sushobh.loggerplugin" // Optional if you use it by class
            implementationClass = "com.sushobh.loggerplugin.ViewModelTransformerPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("com.android.tools.build:gradle:8.9.1")
}
