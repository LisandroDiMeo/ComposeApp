import java.io.ByteArrayOutputStream

plugins {

    /**
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project. Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id("jacoco")
}

subprojects {
//    apply(from = "$rootDir/tools/jacoco/jacoco.gradle")
}

allprojects {
    apply(plugin = "jacoco")
    ext {
        val targetSdk = 33
        val minSdk = 24
        val buildDate = "\"${java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date())}\""
        val versionCode = {
            val code = ByteArrayOutputStream()
            exec {
                commandLine("git","tag","--list")
                standardOutput = code
            }
            code.toString().split("\n").size
        }
        val config = mutableMapOf<String, Any>()
        config["targetSdk"] = targetSdk
        config["minSdk"] = minSdk
        config["buildDate"] = buildDate
        config["versionCode"] = versionCode()
        extensions.extraProperties["config"] = config

        val library = mutableMapOf<String,String>()
        library["androidX"] = "1.8.0"
        library["kotlin-bom"] = "1.8.0"
        library["activity-compose"] = "1.5.1"
        library["lifecycleKtx"] = "2.5.1"
        library["appcompat"] = "1.6.1"
        library["android-material"] = "1.9.0"
        library["compose-bom"] = "2022.10.00"
        library["lifecycle-common"] = "2.6.1"
        library["workX"] = "2.8.1"

        // Coil Compose
        library["coil"] = "2.4.0"

        // I/O
        library["retrofit"] = "2.9.0"
        library["okhttp"] = "4.9.0"
        library["moshi"] = "1.14.0"

        // Test
        library["junit4"] = "4.13.2"
        library["android-junit"] = "1.1.5"
        library["jacoco"] = "0.8.8"

        extensions.extraProperties["library"] = library
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("printTasks", Delete::class) {
    project.tasks.forEach { task -> println("task="+task+" dependsOn="+task.dependsOn) }
}
