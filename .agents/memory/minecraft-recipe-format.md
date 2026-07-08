---
name: Minecraft recipe ingredient format (1.21.2+ / 26.x)
description: Recipe JSON ingredients must be plain item-ID strings, not {"item": "..."} objects, in modern versions.
---

# Recipe ingredient format changed in 1.21.2+

In Minecraft 1.21.2 and later (including the 26.x versioning this project targets),
crafting recipe `ingredients` must be plain strings, not objects.

- Correct: `"minecraft:stick"`, tag `"#minecraft:planks"`, or a list `["minecraft:x", "minecraft:y"]`
- Wrong (silently skipped): `{ "item": "minecraft:stick" }` — the old object form was removed.

**Why:** When an ingredient fails to parse, Minecraft logs an error and skips *that recipe*
without crashing. Symptom: the crafting output slot stays empty even though the result item
is registered and shows up in the creative tab / inventory. Easy to misdiagnose as a
registration or result-format problem.

**How to apply:** When a datapack recipe produces no output but the item is clearly registered,
check the ingredient format first. Also note the `result` block uses `{ "id": "...", "count": N }`
in these versions (not the older `{ "item": "...", "count": N }`).
