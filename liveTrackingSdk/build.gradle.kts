import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("org.jetbrains.dokka") version "2.0.0"

    id("maven-publish")
}

android {
    namespace = "com.kerberos.livetrackingsdk"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
//        compose = true
        aidl = true // Ensure this is true or remove the line (as true is default)
        buildConfig = true // Or remove this line
        dataBinding = true

        // for view binding:
        viewBinding = true
    }

}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.adhamkhwaldeh"
            artifactId = "livetrackingsdk"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("sonatypeUsername") as String?
                password = project.findProperty("sonatypePassword") as String?
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.timber)

    implementation(libs.androidx.datastore.preferences) // Or the latest version
    implementation(libs.androidx.lifecycle.runtime.ktx) // Often used with DataStore for coroutine scopes

}


//subprojects {
//    apply plugin: 'org.jetbrains.dokka'
//}

//dokka {
//    moduleName.set("WeatherGiniSDK")
//    dokkaPublications.html {
//        outputDirectory.set(layout.buildDirectory.dir("dokkaDir"))
//    }
//
//    dokkaSourceSets.main {
//        // Only set the actual Kotlin/Java dirs once
//        sourceRoots.from("src/main/java")
////    dokkaSourceSets.named("main") {
//        includes.from("IntegrationGuide.md")
//        skipEmptyPackages.set(true)
////        includeNonPublic.set(false)
////        includes.from("IntegrationGuide.md")
//        sourceLink {
////            localDirectory.set(file("src/main/java"))
//            remoteUrl("https://github.com/adhamkhwaldeh/WeatherSdk/tree/main/app/src/main/java")
//            remoteLineSuffix.set("#L")
//        }
//
//        reportUndocumented.set(true)          // Warn about undocumented public APIs
//        skipDeprecated.set(true)              // Exclude deprecated elements
//        suppress.set(false)                   // Include suppressed elements
//        sourceRoots.from(file("src/main/java"))
//        sourceRoots.from("src/main/java")
//        jdkVersion.set(17)
//    }
//
//}

tasks.withType<DokkaTaskPartial>().configureEach { // Use DokkaTaskPartial for module-level config
    moduleName.set("LiveTrackingSdk") // Sets the module name

    // Configuration for specific output formats (like HTML)
    // This part of your original config seems to be for an older Dokka version's direct `dokkaPublications`
    // In modern Dokka (1.4+), you usually configure the output directory on the main task
    // or on specific format tasks like dokkaHtml.
    // If 'dokkaPublications.html' is from a very specific Dokka version or a custom setup,
    // this might need adjustment based on that version's DSL.

    // For standard HTML output configuration (Dokka 1.4+):
    // This would typically be on the DokkaTask itself, not DokkaTaskPartial,
    // or handled by the dokkaHtml task specifically.
    // If you are configuring the main HTML output for the module:
    // outputDirectory.set(layout.buildDirectory.dir("dokkaDir/html")) // Example for HTML output

    dokkaSourceSets.named("main") { // Configure the 'main' source set
        // Set the actual Kotlin/Java dirs for sourceRoots only once to avoid duplication
        // and potential issues.
        sourceRoots.setFrom(files("src/main/java")) // Use setFrom to replace existing, or from() to add
        // If you also have Kotlin sources:
        // sourceRoots.from(files("src/main/kotlin"))


        includes.from("IntegrationGuide.md")
        skipEmptyPackages.set(true)
        // includeNonPublic.set(false) // Uncomment if needed, default is false (only public/protected)

        sourceLink {
            // localDirectory.set(file("src/main/java")) // Set this if your sources are not at the project root relative to remoteUrl
            remoteUrl.set(URI("https://github.com/adhamkhwaldeh/WeatherSdk/tree/main/app/src/main/java").toURL())
            remoteLineSuffix.set("#L")
        }

        reportUndocumented.set(true)          // Warn about undocumented public APIs
        skipDeprecated.set(true)              // Exclude deprecated elements (default is true)
        // suppress.set(false) // 'suppress' is not a standard Dokka property.
        // Maybe you meant 'suppressInheritedMembers.set(false)' or similar?
        // Or it's a custom property from a plugin.
        // If you mean to include elements annotated with @suppress, that's usually default.

        // jdkVersion.set(17) // Set the JDK version for parsing Java sources
        // For Dokka 1.6.0+, this is auto-detected from compileJava.sourceCompatibility
        // or can be set if needed for specific cases.
    }
}

// If you need to specifically configure the HTML output directory for the 'dokkaHtml' task:
tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
    outputDirectory.set(layout.buildDirectory.dir("dokkaDir")) // This matches your original outputDirectory
    moduleName.set("LiveTrackingSdk") // Sets the module name

    // Configuration for specific output formats (like HTML)
    // This part of your original config seems to be for an older Dokka version's direct `dokkaPublications`
    // In modern Dokka (1.4+), you usually configure the output directory on the main task
    // or on specific format tasks like dokkaHtml.
    // If 'dokkaPublications.html' is from a very specific Dokka version or a custom setup,
    // this might need adjustment based on that version's DSL.

    // For standard HTML output configuration (Dokka 1.4+):
    // This would typically be on the DokkaTask itself, not DokkaTaskPartial,
    // or handled by the dokkaHtml task specifically.
    // If you are configuring the main HTML output for the module:
    // outputDirectory.set(layout.buildDirectory.dir("dokkaDir/html")) // Example for HTML output

    dokkaSourceSets.named("main") { // Configure the 'main' source set
        // Set the actual Kotlin/Java dirs for sourceRoots only once to avoid duplication
        // and potential issues.
        sourceRoots.setFrom(files("src/main/java")) // Use setFrom to replace existing, or from() to add
        // If you also have Kotlin sources:
        // sourceRoots.from(files("src/main/kotlin"))


        includes.from("IntegrationGuide.md")
        skipEmptyPackages.set(true)
        // includeNonPublic.set(false) // Uncomment if needed, default is false (only public/protected)

        sourceLink {
            // localDirectory.set(file("src/main/java")) // Set this if your sources are not at the project root relative to remoteUrl
            remoteUrl.set(URI("https://github.com/adhamkhwaldeh/WeatherSdk/tree/main/app/src/main/java").toURL())
            remoteLineSuffix.set("#L")
        }

        reportUndocumented.set(true)          // Warn about undocumented public APIs
        skipDeprecated.set(true)              // Exclude deprecated elements (default is true)
        // suppress.set(false) // 'suppress' is not a standard Dokka property.
        // Maybe you meant 'suppressInheritedMembers.set(false)' or similar?
        // Or it's a custom property from a plugin.
        // If you mean to include elements annotated with @suppress, that's usually default.

        // jdkVersion.set(17) // Set the JDK version for parsing Java sources
        // For Dokka 1.6.0+, this is auto-detected from compileJava.sourceCompatibility
        // or can be set if needed for specific cases.
    }
}
//tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaPublicationsHtml") {
//    outputDirectory.set(layout.buildDirectory.dir("dokkaDir")) // This matches your original outputDirectory
//}


// If your Dokka version uses `dokkaPublications` directly in the top-level `dokka { }` block
// (which is less common in recent versions for HTML output directory), it might look like this,
// but the above `tasks.named<DokkaTask>("dokkaHtml")` is more standard for 1.4+.
// dokka { // This is the top-level extension, less common for direct output config now
//     moduleName.set("WeatherGiniSDK")

//     // This specific structure for publications might be from an older Dokka version
//     // or a specific plugin. For standard Dokka 1.4+, output is usually configured
//     // on the task itself (e.g., dokkaHtml).
//     // publications.named("html") { // Assuming 'html' is the name of the publication
//     //    this as org.jetbrains.dokka.gradle.DokkaConfiguration.DokkaPublication // Cast if needed for specific properties
//     //    outputDirectory.set(layout.buildDirectory.dir("dokkaDir"))
//     // }
//     // The rest of your sourceSet configuration would go into a dokkaSourceSets block as above.
// }
