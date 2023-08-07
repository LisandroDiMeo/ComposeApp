plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply(from= "../core.gradle")

android {
    namespace = "com.example.test"
}

dependencies {
    val library = project.ext["library"] as MutableMap<String, Any>
    testImplementation(api("junit:junit:${library["junit4"]}")!!)
    api("androidx.test.ext:junit:${library["android-junit"]}")
//    api("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    api("org.jacoco:org.jacoco.core:${library["jacoco"]}")
    api("org.jacoco:org.jacoco.agent:${library["jacoco"]}")
}
