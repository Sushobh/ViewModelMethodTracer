package com.sushobh.methodlogger

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ViewModelTransformerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withId("com.android.application") {
            val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                variant.instrumentation.transformClassesWith(
                    ViewModelTransformerFactory::class.java,
                    InstrumentationScope.PROJECT
                ) { /* no params */ }
            }
        }
        project.plugins.withId("com.android.library") {
            val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                variant.instrumentation.transformClassesWith(
                    ViewModelTransformerFactory::class.java,
                    InstrumentationScope.PROJECT
                ) { /* no params */ }
            }
        }
    }
}
