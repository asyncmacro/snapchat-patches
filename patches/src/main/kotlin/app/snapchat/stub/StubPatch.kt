package app.snapchat.stub

import app.morphe.patcher.patch.bytecodePatch
import app.snapchat.shared.Constants.COMPATIBILITY_SNAPCHAT

/**
 * Pipeline stub: intentionally changes nothing in the app.
 *
 * Purpose: prove end to end that this source builds in CI, loads in
 * Morphe Manager, and patches the pinned Snapchat cleanly, before any
 * real patch is designed. Delete or replace once real patches land.
 *
 * No fingerprint on purpose: there is no target to match, so patching
 * cannot fail on obfuscation drift. The default no-op execute block runs.
 */
val stubPatch = bytecodePatch(
    name = "Pipeline stub",
    description = "Changes nothing. Verifies this source builds, loads, and patches cleanly.",
) {
    compatibleWith(COMPATIBILITY_SNAPCHAT)

    execute {
        // Intentionally empty: this patch is a no-op by design.
    }
}
