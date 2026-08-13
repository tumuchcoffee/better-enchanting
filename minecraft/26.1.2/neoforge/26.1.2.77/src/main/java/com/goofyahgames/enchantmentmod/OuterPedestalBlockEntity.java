package com.goofyahgames.enchantmentmod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;

public class OuterPedestalBlockEntity extends BlockEntity {

    private ItemStack storedItem = ItemStack.EMPTY;

    public OuterPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BetterEnchanting.OUTER_PEDESTAL_BE_TYPE.get(), pos, state);
    }

    public ItemStack getStoredItem() {
        return storedItem;
    }

    public void setStoredItem(ItemStack stack) {
        this.storedItem = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (!storedItem.isEmpty()) {
            output.store("Item", ItemStack.CODEC, storedItem);
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        storedItem = input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
