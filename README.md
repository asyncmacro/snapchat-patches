# 🧩 asyncmacro Snapchat Patches

Unofficial Snapchat (`com.snapchat.android`) patches for Morphe, built from scratch against one pinned version (see `PINNED_VERSION`).
Not affiliated with Snap Inc. or the Morphe project.

## ❓ About

Snapchat patches for Morphe, developed slowly one at a time and verified on device.

Pinned target: Snapchat `14.20.0.50` (see `PINNED_VERSION`).

### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=asyncmacro/snapchat-patches

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v0.2.0](https://github.com/asyncmacro/snapchat-patches/releases/tag/v0.2.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;1 patches total
<details open>
<summary>📦 Snapchat&nbsp;&nbsp;•&nbsp;&nbsp;1 patch</summary>
<br>

**🎯 Supported versions:**

| 14.20.0.50 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Pipeline stub](#pipeline-stub) | Changes nothing. Verifies this source builds, loads, and patches cleanly. |  |

</details>

<!-- PATCHES_END -->

### 🛠️ Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## 📜 License

asyncmacro Snapchat Patches are licensed under the [GNU General Public License v3.0](LICENSE)
