# AGENTS.md

This file constrains **every** AI agent working in this repository — Claude Code, Replit Agent, Cursor, Codex, Aider, or any other tool that reads `AGENTS.md`. Directives here are binding by default. A live, explicit instruction from the user in the current session overrides a directive here; absent that, follow this file rather than general training knowledge or convenience.

## Hard constraints — never do without explicit, per-instance confirmation

- **Never push to the GitHub remote** (`tumuchcoffee/better-enchanting`). The user reviews mod code personally before it's pushed. This applies to *every* push, not just the first one in a session — prior approval does not carry forward.
- **Never run destructive git operations** (`push --force`, `reset --hard`, `branch -D`, `clean -f`, amending a commit that's already pushed) without confirming first.
- **Never commit or print secrets** (tokens, keys, `.env` contents) into files, chat, or logs.
- **Never edit generated/runtime artifacts** as if they were source: `**/build/`, `.gradle/`, `run/`, `runs/`, `logs/`, `crash-reports/`. If something there looks wrong, fix the source that generates it.

## Build & execution

- Any Gradle task likely to run past ~100s wall-clock (first-time NeoForge/Minecraft decompile, full builds, first-time toolchain downloads) **will not survive repeated foreground/background bash calls** — process trees are killed between separate tool invocations in this sandbox, and restarting just re-runs the same non-resumable step. Run these as a supervised background workflow instead and poll for completion. Details: [.agents/memory/minecraft-mod-build.md](.agents/memory/minecraft-mod-build.md).
- Don't manually install a JDK to fix "missing JDK" errors — Gradle's foojay toolchain resolver auto-downloads what it needs.

## Source of truth — don't guess, look it up

- **Game mechanics** (pedestal tiers, enchant levels 1–5, recipe progressions) are defined in [attached_assets/Better_Enchanting_1783377127066.md](attached_assets/Better_Enchanting_1783377127066.md). If a mechanic isn't specified there, ask — don't invent one and present it as intended design.
- **Minecraft/NeoForge API surface for this exact version** (MC 26.1.2, NeoForge 26.1.2.77) diverges from older versions that dominate general Minecraft-modding knowledge in model training data. Check [.agents/memory/mc-26-1-2-api-changes.md](.agents/memory/mc-26-1-2-api-changes.md) before writing against an API you "remember" from an older version.
- **Recipe JSON conventions**: check [.agents/memory/minecraft-recipe-format.md](.agents/memory/minecraft-recipe-format.md) before authoring new recipe files.

## Scope

- The repo also contains an unused pnpm/TypeScript monorepo scaffold (`artifacts/`, `lib/`, `scripts/`, `pnpm-workspace.yaml`) left over from a template. The actual product is the standalone Java/Gradle mod at `minecraft/26.1.2/neoforge/26.1.2.77/`. Do not wire the mod into the pnpm workspace, add `package.json` scripts for it, or treat `pnpm-workspace.yaml` as relevant — unless a web/mobile artifact is explicitly being added, in which case consult the `pnpm-workspace` skill first.
- Don't restructure the version-encoded module path (`minecraft/<mc-version>/<loader>/<loader-version>/`) — it exists to let multiple Minecraft/loader combinations coexist later.

## Verification before claiming a change is done

- Run `./gradlew compileJava` (or a full build) before reporting a Java change as working. Reading code is not verification.
- For enchanting/pedestal mechanic changes, cross-check behavior against the design doc, not just "it compiles."

## Cross-agent memory hygiene

- Multiple AI tools work in this repo over time. Durable, non-obvious facts (sandbox gotchas, API deltas, format quirks) belong in `.agents/memory/*.md`, indexed in [.agents/memory/MEMORY.md](.agents/memory/MEMORY.md). Check it before re-deriving something the hard way; add to it when you learn something a future session would otherwise re-pay for.
- Update an existing memory file in place rather than creating a near-duplicate.

## Engineering discipline

- No speculative abstractions, feature flags, or future-proofing beyond what's asked.
- Minimal diffs — don't reformat, rename, or refactor code outside the change you were asked to make.
- Comments explain non-obvious *why*, never *what* — well-named code and the design doc already cover *what*.
- Only commit when explicitly asked. Never `--no-verify`, never bypass signing.
