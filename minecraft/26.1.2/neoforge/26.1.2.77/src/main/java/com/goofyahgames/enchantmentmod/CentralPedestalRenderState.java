package com.goofyahgames.enchantmentmod;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class CentralPedestalRenderState extends BlockEntityRenderState {
    public @Nullable ItemStackRenderState item;
    public float yRot;
    public float bobOffset;
}
