import java.net.URL

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
    compileSdk = 36

    defaultConfig {
        applicationId = "karage.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add these for production
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            // For CI/CD, these will be overridden by environment variables or secrets
            storeFile = file("../release/karage-app-keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "android123"
            keyAlias = System.getenv("KEY_ALIAS") ?: "karage-app-key"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "android123"
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            enableUnitTestCoverage = false

            buildConfigField("String", "API_BASE_URL", "\"https://api-dev.karage.app/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("String", "BUILD_TYPE", "\"DEBUG\"")

            resValue("string", "app_name", "Karage Debug")
        }

        create("staging") {
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            buildConfigField("String", "API_BASE_URL", "\"https://api-staging.karage.app/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
            buildConfigField("String", "BUILD_TYPE", "\"STAGING\"")

            resValue("string", "app_name", "Karage Staging")
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            buildConfigField("String", "API_BASE_URL", "\"https://api.karage.app/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
            buildConfigField("String", "BUILD_TYPE", "\"RELEASE\"")

            resValue("string", "app_name", "Karage")
        }
    }

    // Product flavors for different environments/markets
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            buildConfigField("String", "ENVIRONMENT", "\"DEVELOPMENT\"")
            buildConfigField("String", "API_VERSION", "\"v1\"")
        }

        create("prod") {
            dimension = "environment"

            buildConfigField("String", "ENVIRONMENT", "\"PRODUCTION\"")
            buildConfigField("String", "API_VERSION", "\"v1\"")
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
        error +=
            listOf(
                "Accessibility",
                "MissingTranslation",
                "HardcodedText",
                "Internationalization",
                "Typos",
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
        buildConfig = true
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

    dependsOn("testDevDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter =
        listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
        )

    val devDebugTree =
        fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/devDebug") {
            exclude(fileFilter)
        }

    val mainSrc = "${project.projectDir}/src/main/kotlin"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(devDebugTree))
    executionData.setFrom(
        fileTree("${layout.buildDirectory.get()}") {
            include("jacoco/testDevDebugUnitTest.exec")
        },
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

// Dokka V1 configuration for API documentation
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
        named("main") {
            moduleName.set("Karage App")
            reportUndocumented.set(false)
            skipEmptyPackages.set(true)
            skipDeprecated.set(false)

            // Add package documentation
            // val packagesFile = file("src/main/kotlin/packages.md")
            // if (packagesFile.exists()) {
            //     includes.from(packagesFile)
            // }

            // Include source links to GitHub
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(URL("https://github.com/your-username/andriodapp/tree/main/app/src/main/kotlin"))
                remoteLineSuffix.set("#L")
            }
        }
    }
}
