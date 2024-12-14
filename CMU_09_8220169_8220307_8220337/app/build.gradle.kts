import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "ipp.estg.cmu_09_8220169_8220307_8220337"
    compileSdk = 34

    defaultConfig {
        applicationId = "ipp.estg.cmu_09_8220169_8220307_8220337"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Load the values from .properties file
        val properties = Properties()
        properties.load(project.rootProject.file("gradle.properties").inputStream())

        // Configure gradle.properties variables
        buildConfigField("String", "EXERCICEDB_API_KEY", "\"${properties.getProperty("EXERCICEDB_API_KEY")}\"")
        buildConfigField("String", "QUOTES_API_KEY", "\"${properties.getProperty("QUOTES_API_KEY")}\"")
        buildConfigField("String", "MAPS_API_KEY", "\"${properties.getProperty("MAPS_API_KEY")}\"")
        buildConfigField("String", "MAPBOX_DOWNLOADS_TOKEN", "\"${properties.getProperty("MAPBOX_DOWNLOADS_TOKEN")}\"")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true // para usar o buildConfigField
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.storage)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // permitir usar permissões no jetpack compose
    implementation(libs.accompanist.permissions) // permitir usar o sensor de passos

    // Room - to save the data locally and see it in offline mode (cache que funciona como uma base de dados sql)
    implementation ("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")

    // retrofit - para fazer pedidos http e fazer parse de json para classes em kotlin ou java
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // glide - para passar imagens de um link da web para o kotlin (um pouco redundante este, visto que estamos a usar o coil)
    implementation(libs.glide)
    implementation ("com.github.skydoves:landscapist-glide:1.4.8") // GlideCard

    // ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("androidx.fragment:fragment-ktx:1.8.2") // Use the latest version

    // load images from url
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc02")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc02")
    implementation("io.coil-kt.coil3:coil-gif:3.0.0-rc02") // suport with gifs


    // The compose calendar library for Android
//    implementation("com.kizitonwose.calendar:compose:2.6.0")

    // mais icons
    implementation("androidx.compose.material:material-icons-extended:1.7.1")

    // For Camera
    implementation("androidx.camera:camera-camera2:1.1.0-alpha04")
    implementation("androidx.camera:camera-lifecycle:1.1.0-alpha04")
    implementation("androidx.camera:camera-view:1.0.0-alpha21")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    // To use LiveData with Compose
    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")

    // For Background Services
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")

    // Google Maps
    implementation("com.google.maps.android:maps-compose:2.7.3")
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Fiused Location Provider (google play service) - para obter a localização do utilizador
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // KTX library utility
    // KTX for the Maps SDK for Android library
    implementation("com.google.maps.android:maps-ktx:5.1.1")

    // KTX for the Maps SDK for Android Utility Library
    implementation("com.google.maps.android:maps-utils-ktx:5.1.1")
}

// Google Maps
secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}