plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.gradle.plugin)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.gradle.plugin.publish)
    alias(libs.plugins.ktlint)
}

group = "io.github.bhargavms"
version = "1.0.0"

dependencies {
    // Test dependencies
    testImplementation(libs.bundles.kotlin.test)
    testImplementation(libs.bundles.test)
}

gradlePlugin {
    website.set("https://github.com/bhargavms/charles-please")
    vcsUrl.set("https://github.com/bhargavms/charles-please")
    plugins {
        create("charlesplease") {
            id = "io.github.bhargavms.gradle.charlesplease"
            implementationClass =
                "dev.mogra.charlesplease.CharlesPleasePlugin"
            displayName = "Set up Charles proxy"
            description =
                "A Gradle plugin to apply and clear Proxy settings on" +
                "connected Android devices, with auto discover for charles proxy setup"
            tags.set(
                listOf(
                    "proxy",
                    "charles",
                    "android",
                    "emulator",
                    "adb proxy",
                ),
            )
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    }
}

tasks.test {
    useJUnitPlatform()
}

// Handle duplicate files
tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Kotlin JVM toolchain
kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

// ktlint configuration
ktlint {
    version.set("1.2.1")
    debug.set(true)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)

    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
