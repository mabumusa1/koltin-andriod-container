plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("jacoco")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
}

android {
    namespace = "karage.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "karage.app"
        minSdk = 24
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
        debug {
            // Disable built-in coverage to avoid configuration cache issues
            // We'll use JaCoCo plugin manually
            enableUnitTestCoverage = false
        }
    }

    // Configure lint to be strict and prevent poor code quality
    lint {
        // Set to true to abort the build if there are errors
        abortOnError = true
        // Set to true to check all issues, including those that are off by default
        checkAllWarnings = true
        // Make lint report any warning as an error
        warningsAsErrors = true
        // Generate a text report to the console
        textReport = true
        // Generate XML and HTML reports for CI
        xmlReport = true
        htmlReport = true
        // Configure the paths where reports are written
        xmlOutput = file("${layout.buildDirectory.get()}/reports/lint-results.xml")
        htmlOutput = file("${layout.buildDirectory.get()}/reports/lint-results.html")
        // Specify a baseline file - once created, lint will only report new issues
        baseline = file("lint-baseline.xml")
        // Disable lint checks that are too noisy
        disable += listOf("InvalidPackage")
        // List of issues we want to be extra strict about
        error += listOf(
            "Accessibility",
            "MissingTranslation",
            "HardcodedText",
            "Internationalization",
            "Typos"
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Unit testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)

    // Instrumented testing
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.mockito.android)
}

// JaCoCo configuration for test coverage reports
tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

// Task to generate test coverage report
tasks.register<JacocoReport>("jacocoTestReport") {
    group = "reporting"
    description = "Generate Jacoco coverage reports for the debug build."

    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val debugTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    val mainSrc = "${project.projectDir}/src/main/kotlin"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree("${layout.buildDirectory.get()}") {
            include("jacoco/testDebugUnitTest.exec")
        }
    )
}

// Static analysis configuration
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("${project.rootDir}/config/detekt/detekt.yml"))
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
    }
}
