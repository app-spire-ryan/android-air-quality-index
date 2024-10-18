import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.android.serialization)
}

android {
    namespace = "com.hireme.android.library.core.networking"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // val javacRelease: String by project
        val aqiApiProperties: String by project
        val aqiApiPropertiesFileNotFoundException: String by project

        val aqiApiProps = Properties()
        val propertiesFile = File(aqiApiProperties)
        println("props path: ${propertiesFile.path}")

        if (propertiesFile.canRead()) {

            val inputStream = FileInputStream(propertiesFile)
            aqiApiProps.load(inputStream)
            // Close the stream
            inputStream.close()

            // Add to BuildConfig
            buildConfigField("String", "AQI_TOKEN", "${aqiApiProps["TOKEN"]}")

            println("Token added to BuildConfig")

        } else throw FileNotFoundException(aqiApiPropertiesFileNotFoundException)
    }

    buildTypes {

        debug {

        }
        release {

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    api(project(":core:android"))
    api(project(":core:aqi"))

    // Kotlinx
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter)

    // OkHttp
    implementation(libs.okhttp.client)
    implementation(libs.okhttp.loggingInterceptor)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}