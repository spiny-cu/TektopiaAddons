package com.sushiy.tektopiaaddons;

import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;

public interface ITradeableStructure {
    abstract BlockPos tektopiaAddons$getDeliveryChestPos();
    abstract TileEntityChest tektopiaAddons$getDeliveryChest();
}
