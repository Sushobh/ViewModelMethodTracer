package com.sushobh.loggerplugin

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class ViewModelTransformerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.subprojects {

            plugins.withId("com.android.application") {
                setupTransformer()
                afterEvaluate {
                    dependencies.add("implementation", "com.sushobh:method-logger:1.0.1")
                }
            }
            plugins.withId("com.android.library") {
                setupTransformer()
            }
        }
    }

    private fun Project.setupTransformer() {
        val androidComponents = extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                ViewModelTransformerFactory::class.java,
                InstrumentationScope.PROJECT
            ) { /* no params */ }
        }
    }

}
