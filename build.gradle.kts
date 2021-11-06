plugins {
    java
    alias(libs.plugins.kotlin)
}

group = "io.github.aleksandersh"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.std)
    implementation(libs.detekt.api)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.detekt.test)
    testImplementation(libs.junit)
}