# Coding Standards — Better Enchanting

## Package & Naming

- **Package:** `com.goofyahgames.enchantmentmod` — flat, no sub-packages
- **Classes:** PascalCase (`CentralPedestalBlock`)
- **Constants:** `UPPER_SNAKE` (`MODID`, `LOGGER`, `BLOCKS`, `ITEMS`)
- **Mod id:** `enchantment_mod` (snake_case)

---

## NeoForge Registration Patterns

All registration uses **`DeferredRegister`**:

```java
public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(Registries.BLOCK, MODID);
```

Main mod class uses `@Mod` + `@SubscribeEvent` in constructor:

```java
@Mod(BetterEnchanting.MODID)
public class BetterEnchanting {
    public static final String MODID = "enchantment_mod";

    public BetterEnchanting(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }
}
```

---

## Data Serialization (Minecraft 26.x)

Use the new `ValueInput`/`ValueOutput` API, not the old `FriendlyByteBuf`:

```java
// Reading
item = ValueInput.STREAM_CODEC.decode(buf).readMatching(ItemStack.OPTIONAL_STREAM_CODEC);

// Writing
ValueOutput.STREAM_CODEC.encode(buf, ItemStack.OPTIONAL_STREAM_CODEC, item);
```

---

## Block Entities & Renderers

Follow the **render state pattern** (new in 26.x):

- Every `BlockEntity` has a companion `RenderState` class
- Renderer extends `BlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState>`
- Implement `createRenderState()`, `extractRenderState()`, and `render()`
- Sync state via `ClientboundBlockEntityDataPacket`

---

## Item Storage Pattern

Both pedestal types share this interaction model:

- **Right-click with item** → place item on pedestal
- **Right-click empty hand** → retrieve item
- **Destroy block** → drop stored item
- Server-authoritative, synced via block entity update packets

---

## File Placement Conventions

- **New items/blocks:** Register in `BetterEnchanting.java` using the existing `DeferredRegister` fields. Add to the creative tab. Then follow the existing recipe + advancement patterns in `src/main/resources/`.
- **New recipes:** JSON in `data/enchantment_mod/recipes/`, advancement unlock in `data/enchantment_mod/advancement/recipes/`.
- **New textures:** Place in `assets/enchantment_mod/textures/`, reference from block/item model JSON.
- **Localization:** Update `assets/enchantment_mod/lang/en_us.json`.