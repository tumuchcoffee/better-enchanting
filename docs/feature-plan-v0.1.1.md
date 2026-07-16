# Feature Plan v0.1.1

## Overview

This plan covers the first two implementation milestones for the Enchantment Mod. Together they deliver a working end-to-end loop: a multi-tiered pedestal structure that the game can detect, and the first real enchanting recipe (Fire Aspect I) that consumes ingredients and produces an enchanted book automatically.

---

## Task #1 — Pedestal blocks & structure detection

### What & Why
Register the five tiered pedestal blocks and implement multiblock structure detection so the game can identify when a valid enchanting formation is in place. This is the foundation every other feature builds on.

### Done looks like
- Five distinct pedestal blocks exist (Tier 1–5), each with a placeholder texture and name
- Placing a valid formation (center block + 8 outer pedestals in the correct layout) is detected server-side
- The detected tier is determined by the material of the surrounding pedestals
- No enchanting logic yet — detection only

### Out of scope
- Tier material definitions (to be decided — stubs/placeholders only for now)
- Any GUI or player feedback beyond a server log confirming detection
- Enchanting logic

### Steps
1. **Register five pedestal blocks** — one block per tier, each a simple full block with its own registry name and placeholder stone-coloured texture. Remove/replace the existing single `enchanted_pedestal` block.
2. **Define the layout constant** — encode the 8-pedestal formation: in each cardinal direction, step 2 blocks out then 1 block left and 1 block right. Store as a static list of relative BlockPos offsets.
3. **Structure validator** — write a utility that, given the position of the center block, reads the 8 surrounding positions from the world and returns the detected tier (or invalid). Tier is the lowest-tier pedestal material present.
4. **Hook detection into block placement** — listen for the NeoForge block-update event; when a pedestal is placed adjacent to a potential center, run the validator and log the result.

---

## Task #2 — Fire Aspect I enchanting *(depends on Task #1)*

### What & Why
Implement the first working enchantment recipe: Fire Aspect Level 1. When the correct ingredients are placed on the pedestals of a valid Tier 1 formation, the center pedestal outputs a Fire Aspect enchanted book. This proves the full loop end-to-end.

### Recipe
| Ingredient | Qty |
|---|---|
| Crude Brush | 1 |
| Lapis Lazuli | 1 |
| Paper | 1 |
| Fire Charge | 1 |

Placed on the 4 outer pedestals of a Tier 1 formation (order-independent). Result: Fire Aspect I enchanted book on the center pedestal.

### Done looks like
- Player places all four ingredients on the outer pedestals of a Tier 1 formation
- The moment the last item is placed and the structure is complete, the items are consumed and a Fire Aspect I enchanted book appears on the center pedestal automatically
- Nothing happens if the wrong items or wrong tier are present

### Out of scope
- Any enchantment other than Fire Aspect I
- Level 2+ upgrades
- Stone Tablet mechanic (Tier 3, future work)
- Sound or particle effects

### Steps
1. **Item-on-pedestal storage** — give each pedestal block a BlockEntity that holds one ItemStack, with NBT save/load.
2. **Ingredient detection** — when an item is placed on any pedestal, re-run the structure validator; if valid Tier 1, check whether the 4 outer pedestals collectively hold the Fire Aspect I ingredients.
3. **Craft result** — on a successful match, consume all 4 ingredient stacks and place a Fire Aspect I enchanted book into the center pedestal's BlockEntity.
4. **Player interaction** — right-click any pedestal with an item to place it; right-click an occupied pedestal with an empty hand to retrieve it.
