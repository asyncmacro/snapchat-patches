group = "app.asyncmacro"

patches {
    about {
        name = "asyncmacro Snapchat Patches"
        description = "Snapchat patches for Morphe (unofficial)"
        source = "https://github.com/asyncmacro/snapchat-patches"
        author = "asyncmacro"
        contact = "https://github.com/asyncmacro"
        website = "https://github.com/asyncmacro/snapchat-patches"
        license = "GPLv3"
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the APK.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
