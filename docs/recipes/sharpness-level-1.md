# Level 1 Enchantment Recipes

Level 1 enchanted books are crafted at a standard crafting table (shapeless), per the design doc. Ingredients target **easy-to-moderate** acquisition: early-game mining and common mob drops, no diamonds/nether/bosses.

---

## Sharpness I

**Result:** `minecraft:enchanted_book` (Sharpness I)

**Recipe (shapeless, crafting table):**

| # | Ingredient                 | Why it fits "easy to moderate"                            |
| - | -------------------------- | --------------------------------------------------------- |
| 1 | `minecraft:book`         | Paper (sugarcane) + leather — trivial                    |
| 1 | `minecraft:stone_sword`  | 2 iron ingots + stick — the "sharp weapon" thematic core |
| 2 | `minecraft:flint`        | Gravel — very easy                                       |
| 1 | `minecraft:andesite`     | Shallow mining required                                   |
| 1 | `minecraft:lapis_lazuli` | Y < 64 mining — easy, thematic "enchanting" material     |

**Rationale:**

- The **stone sword** anchors the theme — you're extracting the sharpness from a bladed weapon. (Could also be a `stone_sword` for an even cheaper recipe, or `iron_axe`/`iron_pickaxe` variants.)
- **Flint** reinforces the "sharpening" fantasy (flint knapping/sharpening stones).
- **Andesite** adds the only moderate gate: a brief Nether visit. If you want strictly Overworld-only, swap it for `amethyst_shard` (geode mining — also thematic for "magic") or drop it entirely.
- **Lapis** ties the recipe to enchanting thematically and is already abundant.

**Alternative (cheaper) variant:** book + iron sword + 2 flint + 2 lapis (Overworld-only, no Nether required).

**JSON skeleton** (for `data/enchantment_mod/recipes/`):

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    "minecraft:book",
    "minecraft:stone_sword",
    "minecraft:flint",
    "minecraft:flint",
    "minecraft:andesite",
    "minecraft:lapis_lazuli"
  ],
  "result": {
    "id": "minecraft:enchanted_book",
    "count": 1,
    "components": {
      "minecraft:stored_enchantments": {
        "levels": {
          "minecraft:sharpness": 1
        },
        "show_in_tooltip": true
      }
    }
  }
}
```

> Note: the sword will be consumed. If that's too steep, consider adding a custom "Blade Fragment" item later, or accept the sword cost as the level-1 gate (it's only 2 iron).
