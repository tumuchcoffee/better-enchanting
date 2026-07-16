# Feature Plan v0.1.1

## Overview

This plan covers the first two implementation milestones for the Enchantment Mod. Task #1 adds the first working enchantment via a standard crafting table recipe. Task #2 builds the pedestal structure needed for Level 2+ upgrades.

---

## Task #1 — Fire Aspect I crafting recipe

### What & Why
Level 1 enchantments are crafted at a standard crafting table, not on pedestals. This task adds Fire Aspect I as the first working crafting table recipe, giving the player a tangible result from the Crude Brush item.

### Recipe
| Ingredient | Qty |
|---|---|
| Crude Brush | 1 |
| Lapis Lazuli | 1 |
| Paper | 1 |
| Fire Charge | 1 |

Shapeless crafting table recipe. Result: Fire Aspect I enchanted book.

### Done looks like
- Combining the four ingredients in any arrangement in a crafting table produces a Fire Aspect I enchanted book
- The recipe appears in the recipe book under the mod's creative tab
- Crude Brush is consumed by the recipe

### Out of scope
- Any enchantment other than Fire Aspect I
- Level 2+ upgrades (those use the pedestal system — future work)
- Stone Tablet mechanic (Tier 3, future work)

### Steps
1. **Add the shapeless recipe JSON** — create a crafting recipe data file for Fire Aspect I using the four ingredients (Crude Brush, Lapis Lazuli, Paper, Fire Charge) with an enchanted book output tagged with the Fire Aspect I enchantment.
2. **Add recipe advancement** — add the corresponding recipe unlock advancement so the recipe appears in the player's recipe book.

---

## Task #2 — Pedestal blocks & structure detection

### What & Why
Register the five tiered pedestal blocks and implement multiblock structure detection so the game can identify when a valid enchanting formation is in place. This is the foundation for Level 2+ upgrades.

### Done looks like
- Five distinct pedestal blocks exist (Tier 1–5), each with a placeholder texture and name
- Placing a valid formation (center block + 8 outer pedestals in the correct layout) is detected server-side
- The detected tier is determined by the material of the surrounding pedestals
- No enchanting logic yet — detection only

### Out of scope
- Tier material definitions (to be decided — stubs/placeholders only for now)
- Any GUI or player feedback beyond a server log confirming detection
- Enchanting logic

### Crafting Recipes

**Central Pedestal II** — shaped crafting table recipe:

|  | Left | Centre | Right |
|---|---|---|---|
| **Top** | Copper Ingot | Copper Ingot | Copper Ingot |
| **Middle** | Glass | Deepslate | Glass |
| **Bottom** | Deepslate | Deepslate | Deepslate |

### Steps
1. **Register five pedestal blocks** — one block per tier, each a simple full block with its own registry name and placeholder stone-coloured texture. Remove/replace the existing single `enchanted_pedestal` block.
2. **Define the layout constant** — encode the 8-pedestal formation: in each cardinal direction, step 2 blocks out then 1 block left and 1 block right. Store as a static list of relative BlockPos offsets.
3. **Structure validator** — write a utility that, given the position of the center block, reads the 8 surrounding positions from the world and returns the detected tier (or invalid). Tier is the lowest-tier pedestal material present.
4. **Hook detection into block placement** — listen for the NeoForge block-update event; when a pedestal is placed adjacent to a potential center, run the validator and log the result.
