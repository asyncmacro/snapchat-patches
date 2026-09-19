package app.snapchat.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val PACKAGE_NAME = "com.snapchat.android"
    const val APP_NAME = "Snapchat"

    /**
     * Exact versionName recorded in PINNED_VERSION.
     * All patches in this source target only this version.
     */
    const val PINNED_VERSION = "14.20.0.50"

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
