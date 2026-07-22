package com.goofyahgames.enchantmentmod;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CentralPedestalBlock extends BaseEntityBlock {

    public static final MapCodec<CentralPedestalBlock> CODEC = simpleCodec(CentralPedestalBlock::new);

    // Full-width, but only 12 pixels tall from the bottom
    private static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    public CentralPedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<CentralPedestalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CentralPedestalBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                   CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                            BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return SHAPE;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.getBlockEntity(pos) instanceof CentralPedestalBlockEntity pedestal) {
            ItemStack stored = pedestal.getStoredItem();
            if (!stored.isEmpty()) {
                popResource(level, pos, stored);
                pedestal.setStoredItem(ItemStack.EMPTY);
                pedestal.setChanged();
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CentralPedestalBlockEntity pedestal) {
            if (!pedestal.getStoredItem().isEmpty()) {
                if (!level.isClientSide()) {
                    player.getInventory().placeItemBackInInventory(pedestal.getStoredItem());
                    pedestal.setStoredItem(ItemStack.EMPTY);
                    pedestal.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                return InteractionResult.SUCCESS;
            }
            if (!stack.isEmpty()) {
                if (!level.isClientSide()) {
                    pedestal.setStoredItem(stack.copyWithCount(1));
                    stack.shrink(1);
                    pedestal.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CentralPedestalBlockEntity pedestal) {
            ItemStack stored = pedestal.getStoredItem();
            if (!stored.isEmpty()) {
                if (!level.isClientSide()) {
                    player.getInventory().placeItemBackInInventory(stored);
                    pedestal.setStoredItem(ItemStack.EMPTY);
                    pedestal.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
