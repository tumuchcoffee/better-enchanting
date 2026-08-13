# AGENTS.md — Better Enchanting

Hey there, AI agent! 👋 Welcome to the **Better Enchanting** codebase. Here's what you need to know to be productive and avoid stepping on rakes.

---

## Quick Orientation

This is a **Minecraft NeoForge mod** (mod id: `enchantment_mod`, package: `com.goofyahgames.enchantmentmod`) — everything else in this repo is unused scaffolding. The real source lives under `minecraft/26.1.2/neoforge/26.1.2.77/`.

- **Architecture & constraints** → [`docs/architecture.md`](docs/architecture.md)
- **Coding standards & patterns** → [`docs/coding-standards.md`](docs/coding-standards.md)

---

## When Making Changes

1. **Read `docs/coding-standards.md` first** — it covers registration patterns, serialization APIs, renderer conventions, and file placement rules.
2. **Respect registration order.** Blocks must be registered before anything that references them. `NullPointerException: Trying to access unbound value` means you got this wrong.
3. **Stick to the flat package.** All Java sources go in `com.goofyahgames.enchantmentmod` — no sub-packages.
4. **Use the 26.x APIs.** `ValueInput`/`ValueOutput` for data, `BlockEntityRenderer<T, S>` with render states for rendering. Never use old `FriendlyByteBuf`.
5. **Check the design doc** at `attached_assets/Better_Enchanting_1783377127066.md` before adding enchantments — not everything scales to Level 5.

---

## Build Commands

```bash
cd minecraft/26.1.2/neoforge/26.1.2.77
./gradlew compileJava   # quick compile
./gradlew build         # full build (3-4 min first time — don't interrupt!)
```

---

Happy modding! 🎮
