plugins {
    `java-library`

    `maven-publish`
    signing
    alias(libs.plugins.nmcp)
}

group = "net.minestom"
version = System.getenv("MINESTOM_VERSION") ?: "dev"

configurations.all {
    // We only use Jetbrains Annotations
    exclude("org.checkerframework", "checker-qual")
}

repositories {
    mavenCentral()
}

dependencies {
    // Core dependencies
    api(libs.jetbrainsAnnotations)

    // Testing
    testImplementation(libs.bundles.junit)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    (options as? StandardJavadocDocletOptions)?.apply {
        encoding = "UTF-8"

        // Custom options
        addBooleanOption("html5", true)
        addStringOption("-release", "21")
        // Links to external javadocs
        links("https://docs.oracle.com/en/java/javase/${21}/docs/api/")
        links("https://javadoc.io/doc/net.kyori/adventure-api/${libs.versions.adventure.get()}/")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Viewable packets make tracking harder. Could be re-enabled later.
    jvmArgs("-Dminestom.viewable-packet=false")
    jvmArgs("-Dminestom.inside-test=true")
    jvmArgs("-Dminestom.acquirable-strict=true")
    minHeapSize = "512m"
    maxHeapSize = "1024m"
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
