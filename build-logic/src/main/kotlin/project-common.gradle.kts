plugins {
    id("plugin-update-dependencies")
    id("org.jetbrains.kotlin.jvm")
    id("com.diffplug.spotless")
}

project.ext["kotlin_version"] = "1.9.10"
project.ext["kotlinx_coroutines_version"] = "1.7.3"
project.ext["kotlinx_serialization_version"] = "1.6.0"
project.ext["arrow_version"] = "1.2.1"
project.ext["ktor_version"] = "2.3.5"
project.ext["apache_commons_version"] = "1.10.0"
project.ext["junit_jupiter_version"] = "5.10.0"
project.ext["assertj_version"] = "3.24.2"
project.ext["microutils_logging_version"] = "3.0.5"
project.ext["logback_classic_version"] = "1.4.11"

project.ext["test_assertj_version"] = "3.24.2"
project.ext["test_jsonassert_version"] = "1.5.1"
project.ext["test_kotest_assertions_version"] = "1.4.0"
project.ext["test_awaitility_version"] = "4.2.0"

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-reflect:${project.ext["kotlin_version"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${project.ext["kotlinx_coroutines_version"]}")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${project.ext["kotlinx_serialization_version"]}")

        implementation("io.ktor:ktor-server-core:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-server-netty:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-server-content-negotiation:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-serialization-kotlinx-json:${project.ext["ktor_version"]}")

        implementation("io.ktor:ktor-client-core:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-client-apache5:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-client-logging:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-client-content-negotiation:${project.ext["ktor_version"]}")

        implementation("io.ktor:ktor-server-swagger:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-server-cors:${project.ext["ktor_version"]}")


        implementation("io.ktor:ktor-server-call-logging:${project.ext["ktor_version"]}")
        implementation("io.ktor:ktor-server-call-id:${project.ext["ktor_version"]}")

        implementation("io.arrow-kt:arrow-core:${project.ext["arrow_version"]}")
        implementation("io.arrow-kt:arrow-fx-coroutines:${project.ext["arrow_version"]}")

        implementation("io.github.microutils:kotlin-logging-jvm:${project.ext["microutils_logging_version"]}")
        implementation("ch.qos.logback:logback-classic:${project.ext["logback_classic_version"]}")

        implementation("org.junit.jupiter:junit-jupiter:${project.ext["junit_jupiter_version"]}")
        implementation("org.assertj:assertj-core:${project.ext["assertj_version"]}")

        implementation(kotlin("test"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${project.ext["kotlinx_coroutines_version"]}")
        implementation("org.assertj:assertj-core:${project.ext["test_assertj_version"]}")
        implementation("org.skyscreamer:jsonassert:${project.ext["test_jsonassert_version"]}")
        implementation("io.kotest.extensions:kotest-assertions-arrow:${project.ext["test_kotest_assertions_version"]}")
        implementation("io.ktor:ktor-server-test-host:${project.ext["ktor_version"]}")
        implementation("org.awaitility:awaitility-kotlin:${project.ext["test_awaitility_version"]}")
    }
}

testing {
    suites {
        @Suppress("UNUSED_VARIABLE")
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("${project.ext["junit_jupiter_version"]}")
        }
    }
}

spotless {
    kotlin { ktlint() }
    kotlinGradle { ktlint() }
}
