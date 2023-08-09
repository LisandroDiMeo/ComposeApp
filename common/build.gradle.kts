plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply(from = "../core.gradle")

android {
    namespace = "com.example.common"
}

dependencies {
    // Dependency Library
    val library = project.ext["library"] as MutableMap<String, Any>

    // AndroidX & KotlinX
    api("androidx.core:core-ktx:${library["androidX"]}")
    api("androidx.appcompat:appcompat:${library["appcompat"]}")
    api(platform("org.jetbrains.kotlin:kotlin-bom:${library["kotlin-bom"]}"))
    api("androidx.lifecycle:lifecycle-runtime-ktx:${library["lifecycleKtx"]}")
    api("androidx.activity:activity-compose:${library["activity-compose"]}")
    api("androidx.lifecycle:lifecycle-common:${library["lifecycle-common"]}")
    api("androidx.work:work-runtime-ktx:${library["workX"]}")
    api("androidx.compose.runtime:runtime-livedata:1.4.3")


    // Compose & Material
    api(platform("androidx.compose:compose-bom:${library["compose-bom"]}"))
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.ui:ui-tooling")
    api("androidx.compose.ui:ui-tooling-preview")
    api("androidx.compose.material:material")
    api("androidx.compose.material3:material3")
    api("androidx.compose.runtime:runtime")
    api("androidx.compose.runtime:runtime-livedata")
    api("androidx.lifecycle:lifecycle-viewmodel-compose")
    api("androidx.navigation:navigation-compose:${library["navigation"]}")
    api("com.google.android.material:material:${library["android-material"]}")

    api("com.squareup.moshi:moshi:${library["moshi"]}")
    api("com.squareup.retrofit2:retrofit:${library["retrofit"]}")
    api("com.squareup.retrofit2:converter-moshi:${library["retrofit"]}")

    api("com.squareup.okhttp3:okhttp:${library["okhttp"]}")
    api("com.squareup.okhttp3:logging-interceptor:${library["okhttp"]}")

    api("io.coil-kt:coil-compose:${library["coil"]}")

    implementation(project(":test"))

}
