plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Use plugin from the root project (composite build) - no version needed
    id("dev.mogra.charlesplease")
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.example.charlesplease.sample"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.charlesplease.sample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// Charles Please Plugin Configuration
charlesPlease {
    // Example 1: Manual proxy configuration
    host.set("192.168.1.100") // Replace with your computer's IP address
    port.set(8888)
    bypass.set("localhost,127.0.0.1,*.corp.internal")
    serial.set("") // Leave empty to auto-detect single device

    // Example 2: Auto-discovery (uncomment to use)
    // autoDiscover.set(true)
    // bypass.set("localhost,127.0.0.1")

    // Example 3: PAC URL configuration (uncomment to use)
    // pacUrl.set("http://proxy.example.com/proxy.pac")
    // serial.set("emulator-5554") // Target specific device
}
