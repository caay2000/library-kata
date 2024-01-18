plugins {
    `kotlin-dsl`
}

project.ext["kotlin_version"] =  "1.9.22"
project.ext["spotless_version"] =  "6.24.0"
project.ext["dependencies_version"] =  "0.50.0"
project.ext["jib_version"] =  "3.4.0"

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

tasks.wrapper {
    gradleVersion = "8.5"
    distributionType = Wrapper.DistributionType.ALL
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.ext["kotlin_version"]}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${project.ext["kotlin_version"]}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${project.ext["spotless_version"]}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${project.ext["dependencies_version"]}")
    implementation("com.google.cloud.tools:jib-gradle-plugin:${project.ext["jib_version"]}")
}
