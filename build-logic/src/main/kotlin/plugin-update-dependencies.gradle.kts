import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions")
}

fun String.isNonStable(): Boolean = listOf("SNAPSHOT", "RC", "BETA").any { uppercase().contains(it) }

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        candidate.version.isNonStable()
    }
}
