# Enchantment Mod

A NeoForge mod for Minecraft that reimagines the enchanting system around a central enchanting table surrounded by upgradeable pedestals (Levels 1-5, progressing by upgrading pedestal materials).

## Run & Operate

- Mod project lives at `minecraft/26.1.2/neoforge/26.1.2.77/` (path encodes Minecraft version / loader / loader version, to allow other version combos alongside it later)
- Build: `cd minecraft/26.1.2/neoforge/26.1.2.77 && ./gradlew build`
- Compile only: `cd minecraft/26.1.2/neoforge/26.1.2.77 && ./gradlew compileJava`
- GitHub repo: https://github.com/tumuchcoffee/better-enchanting (mod scaffold pushed under `minecraft/26.1.2/neoforge/26.1.2.77/`)

## Stack

- Minecraft 26.1.2, NeoForge 26.1.2.77 (ModDevGradle 2.0.141)
- Java 25 (auto-downloaded by Gradle's foojay toolchain resolver — the Nix env only ships up to JDK 24)
- Gradle 9.2.1 (wrapper)
- This lives alongside an otherwise-unused pnpm monorepo template (`artifacts/`, `lib/`, etc.) — the mod is a standalone Java/Gradle project, not a pnpm workspace package. See the `pnpm-workspace` skill only if web/mobile artifacts are added later.

## Where things live

- `minecraft/26.1.2/neoforge/26.1.2.77/` — the mod's Gradle project (Java sources under `src/main/java/com/goofyahgames/enchantmentmod/`)
- `attached_assets/Better_Enchanting_1783377127066.md` — design doc, source of truth for mod mechanics (pedestal tiers, enchanting levels 1-5)

## Architecture decisions

- Mod id: `enchantment_mod`, group: `com.goofyahgames.enchantmentmod`
- First-time Minecraft decompile/patch/recompile (needed once to produce a compilable NeoForge userdev classpath) takes ~3-4 minutes and must run uninterrupted; in this sandbox, ephemeral bash tool calls get killed between calls, so this step must be run via a temporary Replit workflow (long-running background process) rather than a foreground bash command — see `.agents/memory/minecraft-mod-build.md`.

## Product

Pedestal-based progressive enchanting: craft at Level 1 on a normal crafting table, then upgrade Level 2-5 by placing the item on a center pedestal surrounded by better pedestal materials. Full mechanics still to be implemented — current code is renamed MDK scaffold only (no real pedestal/enchanting logic yet).

## User preferences

- User wants to review mod code personally before anything is pushed to their GitHub repo — do not push without explicit confirmation each time.

## Gotchas

- Long-running Gradle tasks (e.g. first-time Minecraft decompile) must go through a temporary workflow, not a plain foreground/background bash call — see `.agents/memory/minecraft-mod-build.md`.

## Pointers

- See the `pnpm-workspace` skill for workspace structure, TypeScript setup, and package details (only relevant if a web/mobile artifact is added to this project)
