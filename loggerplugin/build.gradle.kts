
import com.vanniktech.maven.publish.SonatypeHost
plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    `kotlin-dsl`
    id("com.vanniktech.maven.publish")
}

group = "com.sushobh"
version = "8.0.0"

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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

gradlePlugin {

    plugins {
        create("loggerPlugin") {
            id = "com.sushobh.method-logger-plugin"
            displayName = "ViewModelMethodCallLogger"
            description = "A Gradle plugin to log viewmodel method calls"
            implementationClass = "com.sushobh.loggerplugin.ViewModelTransformerPlugin"
        }
    }
}

mavenPublishing {
    coordinates("com.sushobh", "method-logger-plugin", "1.0.2")

    pom {
        name.set("Method Logger")
        description.set("Gradle plugin to add log statements to viewmodel public methods")
        inceptionYear.set("2025")
        url.set("https://github.com/Sushobh/ViewModelMethodTracer")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("sushobh")
                name.set("Sushobh")
                url.set("https://github.com/sushobh/")
            }
        }
        scm {
            url.set("https://github.com/Sushobh/ViewModelMethodTracer")
            connection.set("scm:git:git://github.com/sushobh/ViewModelMethodTracer.git")
            developerConnection.set("scm:git:ssh://git@github.com/Sushobh/ViewModelMethodTracer.git")
        }
    }
}
