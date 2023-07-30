plugins {
    // Support convention plugins written in Kotlin.
    // Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    `kotlin-dsl`
}

project.ext["kotlin_version"] =  "1.8.21"
project.ext["spotless_version"] =  "6.20.0"
project.ext["dependencies_version"] =  "0.47.0"

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

tasks.wrapper {
    gradleVersion = "8.2.1"
    distributionType = Wrapper.DistributionType.ALL
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.ext["kotlin_version"]}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${project.ext["kotlin_version"]}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${project.ext["spotless_version"]}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${project.ext["dependencies_version"]}")
}
