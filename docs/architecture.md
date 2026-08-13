# Architecture — Better Enchanting

## What This Project Is

**Better Enchanting** is a Minecraft NeoForge mod that replaces vanilla's RNG enchanting table with a **progressive tiered crafting system**:

| Enchantment Level | How You Get It |
|---|---|
| Level 1 | Shapeless crafting recipe at a crafting table |
| Levels 2–5 | Multiblock structure: a center pedestal surrounded by 8 outer pedestals |

Players upgrade pedestals with better materials (stone → Nether → etc.) rather than building separate setups. The mod id is `enchantment_mod`, package is `com.goofyahgames.enchantmentmod`.

---

## Workspace Layout

This is a **dual-stack monorepo** — two entirely separate build systems coexist side by side:

### 🎮 The Minecraft Mod (this is the real project)
```
minecraft/26.1.2/neoforge/26.1.2.77/
├── build.gradle              ← Gradle 9.2.1 + ModDevGradle 2.0.141
├── gradle.properties         ← mod_version=0.1.2, mc_version=26.1.2
├── src/main/java/com/goofyahgames/enchantmentmod/
│   ├── BetterEnchanting.java          ← Main mod class
│   ├── BetterEnchantingClient.java    ← Client-side entry
│   ├── CentralPedestalBlock.java
│   ├── CentralPedestalBlockEntity.java
│   ├── CentralPedestalBlockEntityRenderer.java
│   ├── CentralPedestalRenderState.java
│   ├── OuterPedestalBlock.java
│   ├── OuterPedestalBlockEntity.java
│   ├── OuterPedestalBlockEntityRenderer.java
│   ├── OuterPedestalRenderState.java
│   └── Config.java
└── src/main/resources/
    ├── assets/enchantment_mod/
    └── data/enchantment_mod/recipes/
```

### 🌐 The pnpm Monorepo (unused scaffolding — ignore unless asked)
```
artifacts/api-server/        ← Express 5 + Drizzle + Pino
artifacts/mockup-sandbox/    ← React 19 + Vite + shadcn/ui
lib/api-spec/                ← OpenAPI 3.1 + Orval codegen
lib/api-zod/                 ← Zod schemas
lib/api-client-react/        ← React Query hooks
lib/db/                      ← Drizzle ORM schema
scripts/                     ← TS utility scripts
```

**The pnpm monorepo is explicitly noted as unused.** Focus on the Minecraft mod unless the user explicitly asks about the web stack.

---

## Build System

```bash
# Compile only
cd minecraft/26.1.2/neoforge/26.1.2.77
./gradlew compileJava

# Full build (decompiles MC, patches, recompiles — slow!)
./gradlew build
```

First build takes 3–4 minutes. **Never interrupt it.** Subsequent builds are faster.

---

## Known Architectural Constraints

1. **Registration order matters.** If something references a `ResourceKey` before `DeferredRegister` fires, you get `NullPointerException: Trying to access unbound value`. Always register blocks *before* anything that references them.

2. **The version-encoded directory** (`minecraft/26.1.2/neoforge/26.1.2.77/`) is intentional — it allows multiple MC/loader version combos to coexist later.

3. **Not all enchants scale to Level 5** (e.g., Mending tops out earlier). Check the design doc in `attached_assets/Better_Enchanting_1783377127066.md` before adding recipes.

4. **Pedestal tiers aren't all implemented yet.** Only Tier 2 central and a single outer pedestal exist. Five tiers are planned.

---

## Next Features to Implement (in order)

1. **Structure detection** — a `StructureValidator` utility that validates the 8-pedestal formation around a center block
2. **Tier detection** — determine tier based on surrounding pedestal materials
3. **Level 2+ enchanting logic** — consume resources, produce upgraded enchanted books
4. **GUI** — the enchanting interface

---

For coding standards and conventions, see [`docs/coding-standards.md`](coding-standards.md).