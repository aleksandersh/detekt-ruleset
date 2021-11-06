rootProject.name = "aleksandersh-detekt-ruleset"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.5.31")
            version("detekt", "1.18.1")

            alias("kotlin").toPluginId("org.jetbrains.kotlin.jvm").versionRef("kotlin")

            alias("kotlin-std").to("org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")
            alias("kotlin-test").to("org.jetbrains.kotlin", "kotlin-test").versionRef("kotlin")
            alias("detekt-api").to("io.gitlab.arturbosch.detekt", "detekt-api").versionRef("detekt")
            alias("detekt-test").to("io.gitlab.arturbosch.detekt", "detekt-test").versionRef("detekt")
            alias("junit").to("junit", "junit").version("4.13.2")
        }
    }
}