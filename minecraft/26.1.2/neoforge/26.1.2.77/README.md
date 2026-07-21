# Enchantment Mod — Project Summary

### Project Goal
A NeoForge mod for Minecraft that reimagines the enchanting system around a central enchanting table surrounded by upgradeable pedestals. Enchantments are crafted at Level 1 on a normal crafting table, then progressively upgraded (Level 2–5) by placing the item on a center pedestal surrounded by better pedestal materials. Full mechanics are documented in `attached_assets/Better_Enchanting_1783377127066.md` (source of truth, not yet implemented).

### Environment & Toolchain
| Setting | Value |
|---|---|
| Minecraft version | `26.1.2` |
| Mod loader | NeoForge `26.1.2.77` (ModDevGradle `2.0.141`) |
| Java | 25 (auto-downloaded by Gradle's foojay toolchain resolver — Nix env only ships up to JDK 24) |
| Gradle | `9.2.1` (wrapper) |
| Mod id | `enchantment_mod` |
| Root namespace / group | `com.goofyahgames.enchantmentmod` |
| Mod version | `0.1.2` |

### Project Structure
- Mod lives at `minecraft/26.1.2/neoforge/26.1.2.77/` — the path deliberately encodes MC version / loader / loader version, so other version combinations can sit alongside it later.
- This is a standalone Java/Gradle project, **not** part of the pnpm monorepo workspace (`artifacts/`, `lib/`, etc. are unused template scaffolding for this project).
- Java sources under `src/main/java/com/goofyahgames/enchantmentmod/`:
  - `BetterEnchanting.java` — main mod class (currently template placeholders: `enchanted_pedestal` block, `arcane_dust` item, creative tab)
  - `BetterEnchantingClient.java` — client-side setup

  - `Config.java` — example config (not yet reflecting real mechanics)

### GitHub
- Repo: https://github.com/tumuchcoffee/better-enchanting (public, owned by user)
- Mod scaffold pushed under `minecraft/26.1.2/neoforge/26.1.2.77/` in the repo (matching local structure)
- Repo's pre-existing `readme.md` (GitHub-created) coexists at repo root alongside the mod's own `README.md`

### Key Decisions
1. **Versioned path structure** — chosen so future Minecraft/loader version combos can live side-by-side without restructuring.
2. **Long Gradle builds run via temporary Replit workflows, not bash** — the sandbox kills background/nohup processes between tool calls, so the one-time Minecraft decompile/patch/recompile step (~3.5 min, required once for NeoForge userdev classpath) must run as a workflow, which persists independently.
3. **User reviews code before any GitHub push** — established preference; pushes only happen on explicit confirmation each time.
4. **`.gitignore`** — root `.gitignore` extended with Gradle/Java patterns (`.gradle/`, `build/`, `*.class`, `*.jar` except the wrapper jar, `run/`, `logs/`, `crash-reports/`) alongside the existing JS/TS monorepo rules.
5. **Logging approach (discussed, not yet implemented)** — keep the existing single shared `BetterEnchanting.LOGGER` (SLF4J via `LogUtils`) rather than per-class loggers; introduce real log-level discipline (`DEBUG` for pedestal/tier state, `INFO` for lifecycle events, `WARN`/`ERROR` for problems) and a `debugLogging` config toggle once pedestal mechanics exist; avoid unthrottled per-tick logging.

### Status
- ✅ Environment fully validated — mod compiles successfully against real Minecraft 26.1.2 / NeoForge 26.1.2.77 sources
- ✅ Scaffold renamed and pushed to GitHub under the correct namespace and folder structure
- ⬜ Not yet started: actual pedestal/enchanting game logic (currently unedited MDK template code only)
- ⬜ Not yet started: logging structure for the real mechanics

### Build & Install
Open the repository in IntelliJ IDEA or Eclipse. If you're missing libraries or hit dependency issues, run:

```
gradlew --refresh-dependencies
```

To reset the build entirely (does not affect source code):

```
gradlew clean
```

### Mapping Names
The MDK uses official Mojang mapping names for Minecraft methods and fields. These are covered by a specific license — refer to the mapping file or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

### Additional Resources
- Community Documentation: https://docs.neoforged.net/
- NeoForged Discord: https://discord.neoforged.net/
