package com.github.caay2000.librarykata.core.common

@Target(AnnotationTarget.FUNCTION)
annotation class TestCase

// Annotation created to avoid problems with ktlint function-naming rule
// It is skipped with .editorconfig property ktlint_function_naming_ignore_when_annotated_with=TestCase
