---
name: minecraft-item-26.1
description: Everything required to add a new simple item to this NeoForge mod targeting Minecraft 26.1.x. Use when the user asks to add an item, create an item, or register a new item in the enchantment mod.
---

# Adding a New Item — Minecraft 26.1.x / NeoForge

This project targets Minecraft 26.1.2 with NeoForge 26.1.2.77. The mod id is `enchantment_mod`, group `com.goofyahgames.enchantmentmod`. All source lives under `minecraft/26.1.2/neoforge/26.1.2.77/`.

Adding a new simple item (e.g. `my_item`) requires **7 files** and **2 Java edits**.

---

## 1. Java registration — `BetterEnchanting.java`

File: `src/main/java/com/goofyahgames/enchantmentmod/BetterEnchanting.java`

### a) Register the item

Add alongside the existing `CRUDE_BRUSH` / `STONE_TABLET` declarations:

```java
public static final DeferredItem<Item> MY_ITEM = ITEMS.registerSimpleItem("my_item");
```

For a custom `Item` subclass, use:

```java
public static final DeferredItem<Item> MY_ITEM = ITEMS.registerItem("my_item", MyItem::new);
```

### b) Add it to the creative tab

Inside the `displayItems` lambda of `ENCHANTMENT_MOD_TAB`:

```java
output.accept(MY_ITEM.get());
```

---

## 2. Item model pointer (NEW in 1.21.2+)

**This file did not exist in older versions — do not skip it.**

Path: `src/main/resources/assets/enchantment_mod/items/my_item.json`

```json
{
  "model": {
    "type": "minecraft:model",
    "model": "enchantment_mod:item/my_item"
  }
}
```

---

## 3. Item model

Path: `src/main/resources/assets/enchantment_mod/models/item/my_item.json`

For a flat 2-D sprite (most items):

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "enchantment_mod:item/my_item"
  }
}
```

For a held tool/weapon use `"parent": "minecraft:item/handheld"` instead.

---

## 4. Texture

Path: `src/main/resources/assets/enchantment_mod/textures/item/my_item.png`

- 16×16 PNG, RGBA.
- Must be present at build time or the game will log a missing-texture warning and show the purple/black checkerboard.

---

## 5. Lang entry

Path: `src/main/resources/assets/enchantment_mod/lang/en_us.json`

Add one line:

```json
"item.enchantment_mod.my_item": "My Item Display Name"
```

---

## 6. Crafting recipe

Path: `src/main/resources/data/enchantment_mod/recipe/my_item.json`

### Shapeless

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "minecraft:stick",
    "minecraft:stone"
  ],
  "result": {
    "id": "enchantment_mod:my_item",
    "count": 1
  }
}
```

### Shaped (3×3 grid, `#` = blank)

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "S S",
    " X ",
    " S "
  ],
  "key": {
    "S": "minecraft:stick",
    "X": "minecraft:stone"
  },
  "result": {
    "id": "enchantment_mod:my_item",
    "count": 1
  }
}
```

### ⚠️ Recipe ingredient format (26.x gotcha)

In 1.21.2+ / 26.x, ingredients **must be plain item-ID strings**, not objects:

```json
// CORRECT
"minecraft:stick"

// WRONG — silently skipped, output slot stays empty
{ "item": "minecraft:stick" }
```

Tags use a `#` prefix: `"#minecraft:planks"`.

The `result` block uses `"id"` (not `"item"`):

```json
"result": { "id": "enchantment_mod:my_item", "count": 1 }
```

---

## 7. Recipe unlock advancement

Path: `src/main/resources/data/enchantment_mod/advancement/recipes/misc/my_item.json`

```json
{
  "parent": "minecraft:recipes/root",
  "criteria": {
    "has_ingredient": {
      "trigger": "minecraft:inventory_changed",
      "conditions": {
        "items": [
          { "items": "minecraft:stick" }
        ]
      }
    },
    "has_the_recipe": {
      "trigger": "minecraft:recipe_unlocked",
      "conditions": {
        "recipe": "enchantment_mod:my_item"
      }
    }
  },
  "requirements": [
    ["has_the_recipe", "has_ingredient"]
  ],
  "rewards": {
    "recipes": ["enchantment_mod:my_item"]
  }
}
```

Pick a meaningful ingredient for `has_ingredient` — typically one of the rarer inputs so the recipe unlocks at the right moment.

---

## Checklist

- [ ] `DeferredItem` declaration in `BetterEnchanting.java`
- [ ] `output.accept(MY_ITEM.get())` in the creative tab lambda
- [ ] `assets/enchantment_mod/items/my_item.json` (model pointer)
- [ ] `assets/enchantment_mod/models/item/my_item.json` (model)
- [ ] `assets/enchantment_mod/textures/item/my_item.png` (texture)
- [ ] `assets/enchantment_mod/lang/en_us.json` (display name entry)
- [ ] `data/enchantment_mod/recipe/my_item.json` (crafting recipe)
- [ ] `data/enchantment_mod/advancement/recipes/misc/my_item.json` (recipe advancement)

---

## Build & verify

```bash
cd minecraft/26.1.2/neoforge/26.1.2.77
./gradlew compileJava      # fast check — catches Java errors
./gradlew build            # full jar
```

See `.agents/memory/minecraft-mod-build.md` for notes on running long Gradle tasks (first-time decompile) via a temporary workflow instead of a plain bash command.
