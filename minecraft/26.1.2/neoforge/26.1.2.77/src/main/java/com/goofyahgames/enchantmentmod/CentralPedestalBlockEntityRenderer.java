package com.goofyahgames.enchantmentmod;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class CentralPedestalBlockEntityRenderer implements BlockEntityRenderer<CentralPedestalBlockEntity, CentralPedestalRenderState> {

    private final ItemModelResolver itemModelResolver;

    public CentralPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CentralPedestalRenderState createRenderState() {
        return new CentralPedestalRenderState();
    }

    @Override
    public void extractRenderState(
            CentralPedestalBlockEntity blockEntity,
            CentralPedestalRenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        ItemStack stack = blockEntity.getStoredItem();
        state.item = null;

        if (!stack.isEmpty()) {
            Level level = blockEntity.getLevel();
            if (level != null) {
                state.item = new ItemStackRenderState();
                this.itemModelResolver.updateForTopItem(state.item, stack, ItemDisplayContext.GROUND, level, null, 0);
            }

            double dx = cameraPosition.x - (blockEntity.getBlockPos().getX() + 0.5);
            double dz = cameraPosition.z - (blockEntity.getBlockPos().getZ() + 0.5);
            state.yRot = (float) Math.toDegrees(Math.atan2(dx, dz));

            state.bobOffset = (float) (Math.sin((System.currentTimeMillis() % 2000) / 2000.0 * Math.PI * 2) * 0.08);
        }
    }

    @Override
    public void submit(CentralPedestalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.item == null) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.25 + state.bobOffset, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
