import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id(libs.plugins.android.library.get().pluginId)
    alias(libs.plugins.kotlin.android)
    id("com.vanniktech.maven.publish")
}


android {
    namespace = "com.sushobh.methodlogger2"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}



mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}

mavenPublishing {
    coordinates("com.sushobh", "method-logger", "1.0.2")

    pom {
        name.set("Method Logger")
        description.set("Library to trace view model methods called")
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