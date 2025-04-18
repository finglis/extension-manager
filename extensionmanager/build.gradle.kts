plugins {
    id("extensionmanager.java-conventions")
    id("extensionmanager.publishing-conventions")
    alias(libs.plugins.javafx)
}

dependencies {
    implementation(libs.slf4j)
    implementation(libs.gson)
    implementation(libs.controlsfx)
    implementation(libs.qupath.fxtras)

    testImplementation(libs.junit)
    testImplementation(libs.logback)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

javafx {
    version = libs.versions.javafx.get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}