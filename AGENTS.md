# AGENTS.md — Snapchat Morphe Patches

Instructions for AI coding agents working in this repo. Read fully before doing anything.

## 1. Goal

Build a **Morphe patch source** for Snapchat (`com.snapchat.android`) **from the
ground up**, against **one pinned Snapchat version**, adding patches slowly, one
at a time.

- Every patch is designed and written here from scratch, guided by the user's
  requests and by analysis of the pinned APK. No feature list is inherited from
  another project.
- Go slowly. One patch, verified on device, committed. Then the next.
- Not affiliated with Snap Inc. or the Morphe project. Do not name the repo or
  bundle in a way that implies Morphe authorship (e.g. "<user> Snapchat patches
  for Morphe").

## 2. Environment constraints (read this twice)

The developer works **in Termux on an Android phone**. Memory, CPU, battery and
storage are scarce. **Heavy builds are the enemy.**

1. **Never run a full build to "see if it works."** See the build budget in §7.
2. Never run `./gradlew build`, `clean`, or `assemble` locally unless the user
   explicitly says so. Never run `clean` without asking.
3. Always pass `--offline` once dependencies are cached, and never run two
   Gradle invocations at once.
4. Prefer reading and grepping over building. Static analysis is free; builds
   are not.
5. Full `.mpp` bundle builds happen in **GitHub Actions**, not on the phone.
6. Don't install large toolchains (Android SDK, Android Studio, emulators) in
   Termux without asking first.
7. Keep clones shallow (`--depth 1`).
8. If a command will take more than ~2 minutes or a lot of RAM, say so and ask
   before running it.

## 3. Repo layout

This folder **is** the patches repo, with `AGENTS.md` at the root.

```
./
├── AGENTS.md
├── FEATURES.md               <- roadmap / status tracker (create in Phase 0)
├── PINNED_VERSION            <- single line: the Snapchat version we target
├── patches/                  <- Kotlin patches + fingerprints (from template)
├── extensions/               <- Java extension code (only when unavoidable)
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/libs.versions.toml
└── .cache/                   <- decompiled Snapchat + notes (gitignored, never committed)
    ├── snapchat-<version>/
    └── notes/
```

Never commit APKs, decompiled output, keystores, or tokens.

## 4. Phase 0 — Bootstrap (one time)

Do these in order. Stop and report if any step fails.

1. **Toolchain check** (report what's missing, don't install blindly): `git`,
   `openjdk-17` (or the JDK the template README requires), `curl`, `unzip`.
   Optional analysis tools: `apktool`, `jadx`.
2. **Initialize from the official template** into this folder:
   ```
   T=$(mktemp -d)
   git clone --depth 1 https://github.com/MorpheApp/morphe-patches-template "$T"
   rm -rf "$T/.git"
   cp -a "$T"/. .
   git init && git add -A && git commit -m "chore: init from morphe-patches-template"
   ```
   Keep our `AGENTS.md` if the copy would overwrite it. Then follow the
   template README:
   - Update `group` and the `About` block in `build.gradle.kts`.
   - Update `README.md` and issue-template links.
   - Use the **template's own** `libs.versions.toml` / `settings.gradle.kts`
     versions. Don't bump Morphe patcher/library versions on your own.
3. **GitHub credentials for Gradle**: the template needs a PAT to pull Morphe
   packages. Follow the template README and put the PAT in
   `~/.gradle/gradle.properties` (user-level). **Never in the repo.**
4. **Create `FEATURES.md`** (§8), add `.cache/` to `.gitignore`.
5. **Apply Termux Gradle settings** (§9).
6. **Do NOT build yet.** The first build is triggered via CI after the first
   real patch exists.

## 5. Phase 1 — Pin the Snapchat version

Snapchat is obfuscated and changes every release, so fingerprints break across
versions. We target **exactly one version** and only move deliberately.

1. Ask the user which Snapchat version they want to target, or which APK they
   have. If they have no preference, suggest a recent stable release they can
   actually obtain as an APK. **Do not guess or invent a version number.**
2. **Get explicit confirmation** before pinning.
3. Record it in `PINNED_VERSION` (one line) and reference it in `README.md`.
4. Every patch declares compatibility with that exact package + version, using
   the template's `compatibleWith(...)` pattern.
5. Changing the pin needs explicit user approval and means re-validating
   **every** existing patch.

## 6. Phase 2 — Reference decompile (once per pinned version)

Do this once, cache it in `.cache/`, and reuse it for every patch.

- Ask the user for the path to the pinned APK (shared storage). Snapchat may
  ship as a split bundle; ask which file to use.
- Decompile once with `apktool` (smali is what fingerprints match) and/or
  `jadx` (readable Java for understanding). Store under
  `.cache/snapchat-<version>/`.
- Use `grep`/`rg` on the cache to find strings, opcodes and call sites. This
  replaces most trial-and-error builds.
- Keep short notes per target in `.cache/notes/` (class, method, anchor strings,
  what the code does).

## 7. Adding a patch — the loop

Work on **one feature at a time**, chosen by the user. Suggest the easiest
first.

### 7.1 Triage the idea

| Tier | Meaning | Do it? |
|------|---------|--------|
| A | Flip a flag / change a return value / skip a call | **Yes, first** |
| B | Needs small injected Java (extension) | After several Tier A patches work |
| C | Needs its own UI, service or large logic | Discuss with the user before starting |

The `extensions/` module may need the Android SDK, which is heavy on Termux, so
extension compiles are CI-only unless the user says otherwise.

### 7.2 Steps

1. **Define** the behavior in 2-3 lines in `FEATURES.md` (what should change,
   where in the app it shows up).
2. **Locate** the target in the `.cache/` decompile. Choose stable anchors
   (string constants, unique opcode patterns, access flags), not obfuscated
   names.
3. **Write the fingerprint** and the patch (Kotlin, `patches/`). Follow the
   template's existing structure and naming. Keep it small.
4. **Dry-check without building**: re-read the code and check the fingerprint
   against the smali by hand/grep. Does it match exactly one method?
5. **Cheap compile check only**: at most one
   `./gradlew :patches:compileKotlin --offline` (adjust to the actual module
   names). This is the only local Gradle task allowed by default.
6. **Commit** (`feat(<patch>): ...`) and push. **CI builds the `.mpp`.**
7. **Test on device**: the user downloads the CI artifact and patches with
   Morphe Manager (or the Morphe CLI in Termux; verify flags with `--help`).
   Wait for the result before starting the next patch.

### 7.3 Build budget

| Action | Where | Allowed |
|--------|-------|---------|
| grep / read / decompile cache lookups | Termux | Always |
| `:patches:compileKotlin --offline` | Termux | Once per patch, after dry-check |
| Full bundle / `.mpp` build | GitHub Actions | Once per patch or batch |
| `clean`, full `build`, `assemble`, extension builds | Termux | **Ask first** |

If a compile check fails, read the error and reason it through **before**
re-running. No "try and rerun" loops. Batch several fixes per run.

## 8. `FEATURES.md` format

One table, one row per feature. Update it in the same commit as the patch.

```
| Feature | Tier | Status | Patch name | Notes |
|---------|------|--------|-----------|-------|
| <feature> | A | todo / wip / done / blocked | <PatchName> | <target + anchor> |
```

Statuses: `todo`, `wip`, `done` (tested on device), `blocked` (say why),
`skipped` (say why).

## 9. Termux Gradle settings

Put these in `~/.gradle/gradle.properties` (user-level, not the repo), then
tune to the device's RAM:

```
org.gradle.jvmargs=-Xmx1536m -XX:+UseSerialGC
org.gradle.workers.max=2
org.gradle.parallel=false
org.gradle.caching=true
kotlin.compiler.execution.strategy=in-process
```

- Run `termux-wake-lock` before long tasks; Android may kill background
  processes.
- Gradle caches and the decompiled APK are large. Check `df -h` first.
- If Gradle can't run `aapt2` on-device, don't fight it. Push the work to CI.

## 10. Coding rules

- Follow the template's patch/fingerprint conventions. Read 2-3 existing
  patches (or the template's examples) before writing one.
- **Fingerprint stability over cleverness**: prefer string constants and
  structural opcode patterns; avoid relying on obfuscated names.
- Every patch: clear `name`, one-line `description`, compatibility with the
  pinned version, default enabled state chosen deliberately.
- Prefer patch-time dex changes over runtime reflection tricks.
- No network calls, telemetry or credential handling added by patches unless
  the feature inherently needs it, and then flag it to the user first.
- Keep diffs small. One feature per commit.

## 11. Licensing

- Keep the template's license and NOTICE files intact (it is GPLv3-based).
- State in the README that this project is unofficial and not affiliated with
  Snap Inc. or Morphe.

## 12. When unsure

- Don't invent Morphe API names, Gradle task names, CLI flags, or version
  numbers. Look them up in the template, its README, or `--help`, or ask.
- If a task needs a heavy build, more storage, or a change to the pinned
  version, **stop and ask**.
- Report briefly: what changed, what was verified, what wasn't tested.

