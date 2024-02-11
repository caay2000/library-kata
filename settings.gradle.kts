pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "library-kata"
include(
    "app-core",
    "app-hexagonal-cqrs",
    "app-event-driven",
    "app-jsonapi",
    "libs:common-arrow",
    "libs:common-cqrs",
    "libs:common-date",
    "libs:common-ddd",
    "libs:common-http",
    "libs:common-id-generator",
    "libs:common-resource-bus",
    "libs:common-test",
    "libs:lib-memory-database",
    "libs:lib-event-bus",
    "libs:lib-dependency-injection"
)
