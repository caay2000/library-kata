pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "library-kata"
include(
    "app-hexagonal-cqrs",
//    "app-event-driven",
    "app-http-client",
    "libs:common-arrow",
    "libs:common-cqrs",
    "libs:common-database",
    "libs:common-date",
    "libs:common-ddd",
    "libs:common-event",
    "libs:common-http",
    "libs:common-id-generator",
    "libs:common-jsonapi",
    "libs:common-serialization",
    "libs:common-test",
    "libs:lib-memory-database",
    "libs:lib-event-bus",
    "libs:lib-dependency-injection"
)
