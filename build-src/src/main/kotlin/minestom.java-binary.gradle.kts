plugins {
    java
    application
    id("com.gradleup.shadow")
}

group = "net.minestom"

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    modularity.inferModulePath = true
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    mergeServiceFiles()
}
