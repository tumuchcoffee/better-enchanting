# Enchantment Mod

Minecraft's enchanting system lets you add magical effects to your tools, weapons, and armor using an enchanting table. You gather experience points by mining, fighting, and crafting, then spend those points at the table to randomly apply enchantments that boost things like damage, durability, or mining speed. Each enchantment has different levels of power, and you can also combine enchanted items on an anvil to stack multiple effects together, though it gets pricier the more you layer them on.

Enchantment Mod allows players to craft enchantments. Here is a summary of the mechanics for the proposed enchanting/pedestal system:

- **Progression and Tiers:** The system revolves around progressive tiers (Levels 1 through 5) for enchantments and their corresponding structures. Not all recipes scale all the way to Level 5; some (like Mending) top out earlier.
- **The Structure Setups:** The system utilizes a central "middle block" (an enchanting table) surrounded by a specific formation of matching pedestal blocks.
- **The Crafting/Upgrading Loop:** To scale an enchantment like Sharpness, a player crafts a basic Level 1 enchantment at a standard crafting table. To reach Level 2, they place that Level 1 book/item onto the center pedestal of a Level 2 formation and surround it with progressively more expensive resources on the outer pedestals. This loop repeats sequentially (Level 2 to Level 3, Level 3 to Level 4) to reach higher tiers.

## Current State

The mod is at early scaffold stage. The project builds and loads into NeoForge without errors, but none of the core pedestal/enchanting mechanics exist yet.

**What is registered and functional:**

- **Enchanted Pedestal** (`enchantment_mod:enchanted_pedestal`) — a plain stone-coloured block. It registers correctly, has a name, and appears in the vanilla Building Blocks creative tab. It has no custom behaviour, no tile entity, and no multiblock awareness.
- **Crude Brush** (`enchantment_mod:crude_brush`) — a simple item with a custom texture. Craftable at a crafting table (shapeless: ink sac + stick + string). Appears in the mod's own creative tab.
- **Stone Tablet** (`enchantment_mod:stone_tablet`) — a simple item with a custom texture. Craftable at a crafting table (shapeless: 2× lapis lazuli + 2× stone). Appears in the mod's own creative tab.
- **Creative tab** — "Enchantment Mod" tab placed after the Combat tab, containing Crude Brush and Stone Tablet.
- **Config screen** — a NeoForge-generated config UI accessible from the Mods menu (currently only exposes leftover MDK placeholder values: a magic number and an item list).

**What does not exist yet:**

- No pedestal tiers (Levels 1–5) — there is one undifferentiated pedestal block only.
- No enchanting table block or multiblock structure detection.
- No enchantment crafting or upgrading logic of any kind.
- No custom item behaviour for Crude Brush or Stone Tablet (they are plain `Item` instances with no right-click or special logic).
- The config file contains only MDK boilerplate; no mod-specific configuration has been added.

