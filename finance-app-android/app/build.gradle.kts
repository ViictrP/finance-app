plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.apollographql.apollo3") version "3.8.5"
    alias(libs.plugins.google.gms.google.services)
    alias (libs.plugins.hilt.application)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.viictrp.financeapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.viictrp.financeapp"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

apollo {
    service("service") {
        packageName.set("com.viictrp.financeapp.graphql")
        mapScalar("BigDecimal", "java.math.BigDecimal", "com.viictrp.financeapp.application.graphql.adapter.BigDecimalAdapter")
        mapScalar("YearMonth", "java.time.YearMonth", "com.viictrp.financeapp.application.graphql.adapter.YearMonthAdapter")
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.apollo.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.core.splashscreen)
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)
    implementation (libs.hilt.navigation.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.lottie.compose)
    implementation(libs.material3.pullrefresh)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.play.services.base)
    implementation(libs.play.services.basement)
    implementation(libs.coil.compose)
    implementation(libs.androidx.foundation)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.animation)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}