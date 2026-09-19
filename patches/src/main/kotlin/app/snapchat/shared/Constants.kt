package app.snapchat.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val PACKAGE_NAME = "com.snapchat.android"
    const val APP_NAME = "Snapchat"

    /**
     * TODO: set to the exact versionName recorded in PINNED_VERSION.
     * Kept as a placeholder until the user confirms the pinned version
     * (AGENTS.md §5: never invent a version number).
     */
    const val PINNED_VERSION = "0.0.0-PIN-ME"

    val COMPATIBILITY_SNAPCHAT = Compatibility(
        name = APP_NAME,
        packageName = PACKAGE_NAME,
        apkFileType = ApkFileType.APK,
        // Snapchat yellow. Tune if needed.
        appIconColor = 0xFFFC00,
        targets = listOf(
            AppTarget(
                version = PINNED_VERSION
            )
        )
    )
}
