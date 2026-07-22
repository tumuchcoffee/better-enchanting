---
name: mc-26-1-2-api-changes
description: Breaking API changes in MC 26.1.2 / NeoForge 26.1.2.77 vs earlier versions — affects block, block entity, and rendering code.
---

# Breaking API Changes — MC 26.1.2 / NeoForge 26.1.2.77

**Why:** Discovered while implementing floating-book block entity renderer. Several APIs changed significantly from older MC/NeoForge versions. Recording to avoid repeat investigation.

---

## Block shape methods

- `getOcclusionShape` signature simplified to **`(BlockState state)`** only — old 3-param form `(BlockState, BlockGetter, BlockPos)` no longer exists in the supertype and will cause `@Override` compile error. Confirmed from decompiled `BlockBehaviour` source in `minecraft-patched-26.1.2.77-sources.jar`.
- `getShape` and `getCollisionShape` still take the full 4-param form `(BlockState, BlockGetter, BlockPos, CollisionContext)`.

## Block interactions

- `InteractionResult` is now a **sealed interface** (not an enum). Constants: `SUCCESS`, `SUCCESS_SERVER`, `CONSUME`, `FAIL`, `PASS`, `TRY_WITH_EMPTY_HAND`.
- `ItemInteractionResult` **does not exist**. `useItemOn` returns `InteractionResult` directly.
- Old `PASS_TO_DEFAULT_BLOCK_INTERACTION` → use `InteractionResult.TRY_WITH_EMPTY_HAND`.
- `sidedSuccess(boolean)` factory method is **gone** — use `SUCCESS` (swings on client) or `SUCCESS_SERVER` (swings on server) directly.
- All `Block` subclasses (including `BaseEntityBlock`) must implement **`codec()`**:
  ```java
  public static final MapCodec<MyBlock> CODEC = simpleCodec(MyBlock::new);
  @Override public MapCodec<MyBlock> codec() { return CODEC; }
  ```

## Block registration

- **Never use `BLOCKS.register(name, Supplier)` for custom block subclasses.** That overload does NOT inject the block's `ResourceKey` into `BlockBehaviour.Properties`, causing a runtime NPE "Block id not set" when `BlockEntityType` validates the block, and leaving the `DeferredHolder` unbound so subsequent `.get()` calls throw "unbound value".
- Use `BLOCKS.registerBlock(name, Function<BlockBehaviour.Properties, B>)` instead — this injects `.setId(key)` before calling the factory.
- `BLOCKS.registerSimpleBlock(name, UnaryOperator<Properties>)` also works the same way for plain `Block` subclasses.
- `BlockEntityType.Builder` does **not** exist in 26.1.x (NeoForge source javadoc comments reference it but it was removed). Use `new BlockEntityType<>(factory, block1, block2, ...)` direct constructor.

**Why:** `register(name, Supplier)` bypasses NeoForge's ID-injection step. The `BlockEntityType` constructor validates each block via `BuiltInRegistries.BLOCK`, and a block without its `ResourceKey` set on its `Properties` is considered unregistered, producing NPE + cascade "unbound value" errors at world load.

## Block entities

- `saveAdditional` and `loadAdditional` signatures changed: they take `ValueOutput`/`ValueInput` (package `net.minecraft.world.level.storage`), **not** `CompoundTag`/`HolderLookup.Provider`.
  ```java
  output.store("Item", ItemStack.CODEC, storedItem);  // save
  input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);  // load
  ```
- `getUpdateTag(HolderLookup.Provider)` still exists and still returns `CompoundTag`. Use `return saveWithoutMetadata(registries);` — it internally calls `saveAdditional(ValueOutput)` so stored values are included.
- `getUpdatePacket()` unchanged — `ClientboundBlockEntityDataPacket.create(this)` still works.
- `BlockEntityType.Builder` **does not exist**. Use direct constructor: `new BlockEntityType<>(supplier, block1, block2, ...)`.

## Items

- `EnchantedBookItem` class **removed**. Check `stack.getItem() == Items.ENCHANTED_BOOK` instead.

## Block Entity Rendering (major overhaul)

The `BlockEntityRenderer` interface now has **three methods** and requires a `BlockEntityRenderState` subclass:

```java
public interface BlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState> {
    S createRenderState();
    void extractRenderState(T be, S state, float partialTicks, Vec3 cameraPos, @Nullable CrumblingOverlay breakProgress);
    void submit(S state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera);
}
```

- Render state subclass extends `BlockEntityRenderState` (package `net.minecraft.client.renderer.blockentity.state`). Base class provides `blockPos`, `lightCoords`, etc.
- **Item rendering**: obtain `ItemModelResolver` from `context.itemModelResolver()` in constructor. In `extractRenderState`, call `itemModelResolver.updateForTopItem(itemStackRenderState, stack, ItemDisplayContext.GROUND, level, null, seed)`. In `submit`, call `itemStackRenderState.submit(poseStack, collector, lightCoords, OverlayTexture.NO_OVERLAY, 0)`.
- `LightTexture` no longer needed — use `state.lightCoords` from `BlockEntityRenderState` directly.
- `Camera.getPosition()` → `camera.position()` (method renamed).
- Camera's Y rotation is available as `camera.yRot` on `CameraRenderState`.
- `Minecraft.getItemRenderer()` method gone — use `ItemModelResolver` pattern above.
- `BlockEntityRendererProvider<T, S>` is a functional interface `(Context) -> BlockEntityRenderer<T, S>`. The constructor reference `MyRenderer::new` satisfies it as long as `MyRenderer` implements `BlockEntityRenderer<T, S>`.

**How to apply:** Any time you add a new block entity renderer or touch existing rendering code in this version, use the three-method pattern above, not the old single `render()` method.
