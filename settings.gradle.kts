pluginManagement {
    includeBuild("build-logic")
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "project-skeleton"
include(
    "app",
    "libs:common-arrow",
    "libs:common-cqrs",
    "libs:common-date",
    "libs:common-ddd",
    "libs:common-event",
    "libs:common-http",
    "libs:common-id-generator",
    "libs:common-serialization",
    "libs:common-test",
    "libs:lib-memory-database",
    "libs:lib-event-bus",
    "libs:lib-dependency-injection"
)
