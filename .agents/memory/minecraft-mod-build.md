---
name: minecraft-mod-build
description: How to run long, first-time NeoForge/Minecraft Gradle builds in this sandbox without losing progress
---

Ephemeral `bash` tool calls in this sandbox get their whole process tree killed when the tool call's session ends — `nohup ... & disown` does NOT survive across separate bash tool invocations (confirmed: a `sleep 90` foreground wait kept a gradle build alive within one call, but by the next separate call 0 java processes remained). This makes any single Gradle task that needs several uninterrupted minutes (e.g. a first-time NeoForge/NeoForm Minecraft decompile+patch+recompile, which took ~3.5 min for MC 26.1.2) unrunnable via repeated foreground/background bash calls — each restart from disk cache just re-does the same non-resumable decompile step and never finishes.

**Fix:** run the long build command as a temporary Replit workflow instead (`configureWorkflow` in the code_execution sandbox). Workflows are supervised as persistent background processes and are NOT tied to a bash tool call's lifetime, so they run to completion uninterrupted. Poll with `getWorkflowStatus` every ~1-2 min, then `removeWorkflow` once it reports `state: "finished"` with `BUILD SUCCESSFUL`.

**Why:** discovered while setting up a NeoForge mod dev environment — direct bash (foreground timeout, and background nohup+poll-in-loop across separate tool calls) could not get a ~3.5 minute Minecraft decompile step to finish; wrapping the same `./gradlew compileJava` command in a throwaway workflow let it run to completion on the first uninterrupted attempt.

**How to apply:** any time you need to run a Gradle/build command likely to exceed ~100s of real wall-clock work in one shot (first-time toolchain downloads, first-time decompiles, full test suites), prefer a temporary workflow over bash, and remove the workflow once done so it doesn't linger in the workflow list.

Separately: Gradle's foojay toolchain resolver can auto-download JDKs not present in the Nix environment (e.g. downloaded JDK 25 automatically even though Nix only had up to jdk24) — no manual JDK install needed for newer Minecraft/NeoForge Java requirements.
