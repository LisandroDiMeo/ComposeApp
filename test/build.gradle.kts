plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply(from = "../core.gradle")

android {
    namespace = "com.example.test"
}

dependencies {
    val library = project.ext["library"] as MutableMap<String, Any>
    testImplementation(api("junit:junit:${library["junit4"]}")!!)
    api("androidx.test.ext:junit:${library["android-junit"]}")
    api(platform("org.jetbrains.kotlin:kotlin-bom:${library["kotlin-bom"]}"))
    implementation("androidx.core:core-ktx:${library["androidX"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${library["lifecycleKtx"]}")
    implementation(platform("androidx.compose:compose-bom:${library["compose-bom"]}"))
    implementation("androidx.compose.runtime:runtime")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    api("androidx.arch.core:core-testing:2.1.0")
//    api("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    api("org.jacoco:org.jacoco.core:${library["jacoco"]}")
    api("org.jacoco:org.jacoco.agent:${library["jacoco"]}")
}
